package com.lukelorusso.presentation.ui.camera

import android.content.pm.PackageManager
import android.graphics.drawable.GradientDrawable
import android.os.*
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.usecase.base.Logger
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.databinding.FragmentCameraBinding
import com.lukelorusso.presentation.error.ErrorMessageFactory
import com.lukelorusso.presentation.extensions.*
import com.lukelorusso.presentation.ui.base.ContentState
import com.lukelorusso.presentation.ui.main.MainActivity
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration
import io.fotoapparat.parameter.Flash
import io.fotoapparat.selector.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import io.fotoapparat.log.Logger as FotoApparatLogger

class CameraFragment : Fragment(R.layout.fragment_camera) {

    companion object {
        val TAG: String = this::class.java.simpleName
        private const val INIT_ZOOM_VALUE = 10
        private const val MAX_ZOOM_VALUE = 100

        fun newInstance(): CameraFragment = CameraFragment()
    }

    // View
    private lateinit var binding : FragmentCameraBinding
    private val viewModel by viewModel<CameraViewModel>()
    private val errorMessageFactory by inject<ErrorMessageFactory>()
    private val logger by inject<Logger>()

    // Properties
    private var isFrontCamera = false
    private val cameraConfiguration by lazy {
        CameraConfiguration(
            previewResolution = firstAvailable(
                wideRatio(highestResolution()),
                standardRatio(highestResolution())
            ),
            flashMode = off(),
            focusMode = firstAvailable(
                continuousFocusPicture(),
                autoFocus(),
                fixed()
            ),
            pictureResolution = highestResolution()
        )
    }
    private var camera: Fotoapparat? = null

    fun onBackPressHandled(): Boolean {
        return when {
            isActive() && isToolbarColorVisible() -> {
                hideToolbarColor()
                true
            }

            else ->
                false
        }
    }

    override fun onStop() {
        super.onStop()
        camera?.stop()
    }

    override fun onStart() {
        super.onStart()
        camera?.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initRouter(
            requireActivity() as AppCompatActivity,
            fragment = this
        ) // If there's a router, initialize it here
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        if (savedInstanceState != null) {
            val duration = resources.getInteger(R.integer.splash_screen_duration)
            Handler(Looper.getMainLooper())
                .postDelayed({ viewModel.getLastZoomValue() }, duration.toLong())
        }
    }

    // region RENDER
    private fun render(data: CameraUiState) {
        showLoading(data.contentState == ContentState.LOADING)
        renderInitCamera(data)
        renderColorResult(data.color)
        renderSnack(data.contentState.error?.let(errorMessageFactory::getLocalizedMessage))
    }

    private fun renderSnack(messageError: String?) {
        showToolbarColor(messageError)
    }

    private fun renderInitCamera(data: CameraUiState) {
        data.lastLensPosition?.also { position ->
            if (camera == null) {
                camera = Fotoapparat(
                    context = requireContext(),
                    view = binding.cameraView,
                    focusView = binding.focusView,
                    logger = object : FotoApparatLogger {
                        override fun log(message: String) {
                            logger.log { "Camera message: $message" }
                        }
                    },
                    lensPosition = if (position == 0) back() else front(),
                    cameraConfiguration = cameraConfiguration,
                    cameraErrorCallback = { Timber.e("Camera error: $it") }
                ).apply { start() }
                isFrontCamera = position == 1
                checkFrontCamera()
                checkCameraCapabilities()

                val duration = resources.getInteger(R.integer.fading_effect_duration_default)
                Handler(Looper.getMainLooper()).postDelayed({
                    (activity as? MainActivity)?.hideSplashScreen()
                }, duration.toLong())
            }

            binding.cameraZoomSeekBar.apply {
                maxValue = MAX_ZOOM_VALUE
                setOnProgressChangeListener { progressValue ->
                    camera?.setZoom(progressValue.toFloat().div(MAX_ZOOM_VALUE))
                }
                setOnReleaseListener { progressValue ->
                    viewModel.setLastZoomValue(progressValue)
                }
                visibility = View.VISIBLE
            }
        }

        data.lastZoomValue?.also {
            binding.cameraZoomSeekBar.progress = if (it == -1) INIT_ZOOM_VALUE else it
        }

        data.cameraCapabilities?.also { capabilities ->
            if (capabilities.flashModes.contains(Flash.On)) {
                binding.inclToolbarCameraTop.root.visibility = View.VISIBLE
                binding.inclToolbarCameraTop.toolbarFlashButton.visibility = View.VISIBLE
            } else {
                binding.inclToolbarCameraTop.toolbarFlashButton.visibility = View.GONE
            }

            binding.cameraZoomSeekBar.visibility =
                if (capabilities.zoom.toString() == "Zoom.FixedZoom")
                    View.GONE
                else
                    View.VISIBLE
        }
    }

    private fun renderColorResult(color: Color?) {
        color?.also { result -> showToolbarColor(result) }
    }
    // endregion

    private fun initView() {
        // Create a new coroutine since repeatOnLifecycle is a suspend function
        lifecycleScope.launch {
            // The block passed to repeatOnLifecycle is executed when the lifecycle
            // is at least STARTED and is cancelled when the lifecycle is STOPPED.
            // It automatically restarts the block when the lifecycle is STARTED again.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Safely collect from locationFlow when the lifecycle is STARTED
                // and stops collection when the lifecycle is STOPPED
                viewModel.uiState.collect { uiState ->
                    render(uiState)
                }
            }
        }

        view?.statusBarHeight()?.also { statusBarHeight ->
            if (statusBarHeight > 0) binding.inclToolbarCameraTop.toolbarTopSpacer.apply {
                layoutParams = LinearLayout.LayoutParams(
                    this.width,
                    statusBarHeight
                )
            }
        }

        binding.inclToolbarCameraBottom.toolbarCameraButton.setOnClickListener {
            camera?.also { camera ->
                showLoading(true)
                camera.takePicture().toBitmap().whenAvailable { bitmapPhoto ->
                    bitmapPhoto?.also { result ->
                        Handler(Looper.getMainLooper()).post {
                            val bitmap = result.bitmap
                            val pixel = bitmap.getAveragePixel(viewModel.uiState.value.pixelNeighbourhood)
                            viewModel.decodeColor(pixel.pixelColorToHash())
                        }
                    }
                }
            }
        }

        binding.inclToolbarCameraTop.toolbarSwitchCameraButton.setOnClickListener {
            camera?.also { camera ->
                camera.switchTo(
                    lensPosition = if (isFrontCamera) back() else front(),
                    cameraConfiguration = CameraConfiguration()
                )
                isFrontCamera = !isFrontCamera
                viewModel.setLastLensPosition(isFrontCamera.toInt())
                initToolbarTop()
                checkCameraCapabilities()
                camera.setZoom(binding.cameraZoomSeekBar.progress.toFloat().div(MAX_ZOOM_VALUE))
            }
        }

        binding.inclToolbarCameraTop.toolbarFlashButton.setOnClickListener {
            camera?.also { camera ->
                camera.getCurrentParameters().whenAvailable { cameraParameters ->
                    val isFlashOn = cameraParameters?.flashMode == Flash.Torch
                    camera.updateConfiguration(
                        cameraConfiguration.copy(
                            flashMode = if (isFlashOn) off() else torch()
                        )
                    )
                    initToolbarTop(!isFlashOn)
                }
            }
        }

        binding.inclToolbarCameraBottom.toolbarInfoButton.setOnClickListener {
            viewModel.gotoInfo()
        }

        binding.inclToolbarCameraBottom.toolbarHistoryButton.setOnClickListener {
            viewModel.gotoHistory()
        }
    }

    private fun initToolbarTop(isFlashOn: Boolean = false) {
        binding.inclToolbarCameraTop.toolbarSwitchCameraButton.setImageResource(
            if (isFrontCamera) R.drawable.camera_rear_white
            else R.drawable.camera_front_white
        )
        binding.inclToolbarCameraTop.toolbarFlashButton.setImageResource(
            if (isFlashOn) R.drawable.flash_off_white
            else R.drawable.flash_on_white
        )
    }

    private fun checkFrontCamera() {
        if (requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            binding.inclToolbarCameraTop.root.visibility = View.VISIBLE
            binding.inclToolbarCameraTop.toolbarSwitchCameraButton.visibility = View.VISIBLE
        }
    }

    private fun checkCameraCapabilities() {
        Handler(Looper.getMainLooper()).post {
            camera?.also { camera ->
                camera.getCapabilities().whenAvailable { capabilities ->
                    viewModel.updateUiState { it.copy(cameraCapabilities = capabilities) }
                }
            }
        }
    }

    private fun showToolbarColor(color: Color) {
        binding.inclToolbarColor.root.fadeInView()

        binding.inclToolbarColor.colorPreviewPanel.visibility = View.VISIBLE

        (binding.inclToolbarColor.colorPreviewPanel.background as? GradientDrawable)
            ?.setColor(color.originalColorHex().hashColorToPixel())

        binding.inclToolbarColor.colorMainLine.visibility = View.VISIBLE
        binding.inclToolbarColor.colorMainLine.text = color.colorName

        binding.inclToolbarColor.colorTopLine.visibility = View.VISIBLE
        val topLineText = color.originalColorHex()
        binding.inclToolbarColor.colorTopLine.text = topLineText

        binding.inclToolbarColor.colorBottomLine.visibility = View.VISIBLE
        binding.inclToolbarColor.colorBottomLine.text = color.toRGBPercentString()

        binding.inclToolbarColor.root.setOnClickListener { viewModel.gotoPreview(color) }
    }

    private fun showToolbarColor(errorMessage: String?) {
        errorMessage?.also { msg ->
            binding.inclToolbarColor.root.fadeInView()

            binding.inclToolbarColor.colorPreviewPanel.visibility = View.GONE

            binding.inclToolbarColor.colorTopLine.visibility = View.VISIBLE
            binding.inclToolbarColor.colorTopLine.text = msg

            binding.inclToolbarColor.colorMainLine.visibility = View.GONE

            binding.inclToolbarColor.colorBottomLine.visibility = View.GONE

            binding.inclToolbarColor.root.setOnClickListener(null)
        }
    }

    private fun isToolbarColorVisible(): Boolean {
        return binding.inclToolbarColor.root.visibility == View.VISIBLE
    }

    private fun hideToolbarColor() {
        if (isToolbarColorVisible()) {
            val duration = resources.getInteger(R.integer.fading_effect_duration_fast)
            binding.inclToolbarColor.root.fadeOutView(duration)
        }
    }

    private fun showLoading(show: Boolean) =
        showLoading(binding.inclToolbarCameraBottom.toolbarProgressBar.root, show)

    private fun showLoading(loading: View, visible: Boolean) {
        if (visible) {
            hideToolbarColor()
            binding.inclToolbarCameraBottom.toolbarCameraButton.visibility = View.GONE
            loading.visibility = View.VISIBLE
        } else {
            loading.visibility = View.GONE
            binding.inclToolbarCameraBottom.toolbarCameraButton.visibility = View.VISIBLE
        }
    }

    fun reloadData() {
        viewModel.reloadData(isFrontCamera.toInt(), binding.cameraZoomSeekBar.progress)
    }

}
