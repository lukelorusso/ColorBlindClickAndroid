package com.lukelorusso.presentation.scenes.camera

import android.content.pm.PackageManager
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lukelorusso.data.helper.TimberWrapper
import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.databinding.FragmentCameraBinding
import com.lukelorusso.presentation.extensions.*
import com.lukelorusso.presentation.helper.TrackerHelper
import com.lukelorusso.presentation.scenes.base.view.ABaseDataFragment
import com.lukelorusso.presentation.scenes.base.view.LoadingState
import com.lukelorusso.presentation.scenes.main.MainActivity
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration
import io.fotoapparat.log.Logger
import io.fotoapparat.parameter.Flash
import io.fotoapparat.selector.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

class CameraFragment : ABaseDataFragment<CameraViewModel, CameraData>(
        CameraViewModel::class.java
) {

    companion object {
        val TAG: String = CameraFragment::class.java.simpleName
        private const val INIT_ZOOM_VALUE = 10
        private const val MAX_ZOOM_VALUE = 100

        fun newInstance(): CameraFragment = CameraFragment()
    }

    @Inject
    lateinit var trackerHelper: TrackerHelper

    // Intents
    private val intentGetColor = PublishSubject.create<Pair<String, String>>()
    private val intentSetLastLensPosition = PublishSubject.create<Int>()

    // View
    private lateinit var binding: FragmentCameraBinding // This property is only valid between onCreateView and onDestroyView

    // Properties
    private var homeUrl: String = ""
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

    fun backPressHandled(): Boolean {
        return when {
            isToolbarColorVisible() -> {
                hideToolbarColor()
                true
            }
            else -> false
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
        activityComponent.inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        FragmentCameraBinding.inflate(inflater, container, false).also { inflated ->
            binding = inflated
            return binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        viewModel.observe(viewLifecycleOwner) {
            if (it != null) render(it)
        }
    }

    // region RENDER
    override fun render(data: CameraData) {
        showProgress(
                data.loadingState == LoadingState.LOADING ||
                        data.loadingState == LoadingState.RETRY
        )
        renderHomeUrl(data.homeUrl)
        renderInitCamera(data.lastLensPosition)
        renderColorResult(data.color)
        showToolbarColor(data.errorMessage)
        showToolbarColor(data.snackMessage)
        renderPersistenceException(data.isPersistenceException)
    }

    private fun renderHomeUrl(homeUrl: String?) {
        homeUrl?.also { this.homeUrl = it }
    }

    private fun renderInitCamera(lastLensPosition: Int?) {
        lastLensPosition?.also { position ->
            camera = Fotoapparat(
                    context = requireContext(),
                    view = binding.cameraView,
                    focusView = binding.focusView,
                    logger = object : Logger {
                        override fun log(message: String) {
                            TimberWrapper.d { "Camera message: $message" }
                        }
                    },
                    lensPosition = if (position == 0) back() else front(),
                    cameraConfiguration = cameraConfiguration,
                    cameraErrorCallback = { Timber.e("Camera error: $it") }
            ).apply { start() }
            isFrontCamera = position == 1
            checkFrontCamera()
            checkCameraCapabilities()

            binding.cameraZoomSeekBar.apply {
                maxValue = MAX_ZOOM_VALUE
                setOnProgressChangeListener { progressValue ->
                    camera?.setZoom(progressValue.toFloat().div(MAX_ZOOM_VALUE))
                }
                progress = INIT_ZOOM_VALUE
                visibility = View.VISIBLE
            }

            val duration = resources.getInteger(R.integer.fading_effect_duration_default)
            Handler(Looper.getMainLooper()).postDelayed({
                (activity as? MainActivity)?.hideSplashScreen()
            }, duration.toLong())
        }
    }

    private fun renderColorResult(color: Color?) {
        color?.also { result -> showToolbarColor(result) }
    }

    private fun renderPersistenceException(isPersistenceException: Boolean?) {
        isPersistenceException?.alsoTrue {
            trackerHelper.track(activity, TrackerHelper.Actions.PERSISTENCE_EXCEPTION)
        }
    }
    // endregion

    private fun initView() {
        subscribeIntents()

        binding.inclToolbarCameraBottom.toolbarCameraButton.setOnClickListener {
            showProgress(true)
            camera?.also { camera ->
                camera.takePicture().toBitmap().whenAvailable { bitmapPhoto ->
                    bitmapPhoto?.also { result ->
                        Handler(Looper.getMainLooper()).post {
                            val bitmap = result.bitmap
                            val pixel = bitmap.getPixel(
                                    bitmap.width / 2,
                                    bitmap.height / 2
                            )
                            intentGetColor.onNext(
                                    Pair(
                                            pixel.pixelColorToHash(),
                                            activity?.getDeviceUdid() ?: ""
                                    )
                            )
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
                intentSetLastLensPosition.onNext(if (isFrontCamera) 1 else 0)
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

    private fun subscribeIntents() {
        val loadData = Observable.just(Unit).flatMap { unit ->
            Observable.merge(
                    viewModel.intentGetHomeUrl(unit),
                    viewModel.intentGetLastLensPosition(unit)
            )
        }
        val getColor = intentGetColor.flatMap { viewModel.intentGetColor(it) }
        val setLastLensPosition = intentSetLastLensPosition
                .flatMap { viewModel.intentSetLastLensPosition(it) }

        viewModel.subscribe(loadData, getColor, setLastLensPosition)
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
                    if (capabilities?.flashModes?.contains(Flash.On) == true) {
                        binding.inclToolbarCameraTop.root.visibility = View.VISIBLE
                        binding.inclToolbarCameraTop.toolbarFlashButton.visibility = View.VISIBLE
                    } else {
                        binding.inclToolbarCameraTop.toolbarFlashButton.visibility = View.GONE
                    }

                    binding.cameraZoomSeekBar.visibility =
                            if (capabilities?.zoom.toString() == "Zoom.FixedZoom")
                                View.GONE
                            else
                                View.VISIBLE
                }
            }
        }
    }

    private fun showToolbarColor(color: Color) {
        binding.inclToolbarColor.root.fadeInView()

        binding.inclToolbarColor.colorPreviewPanel.visibility = View.VISIBLE

        (binding.inclToolbarColor.colorPreviewPanel.background as? GradientDrawable)
                ?.setColor(color.colorHex.hashColorToPixel())

        binding.inclToolbarColor.colorMainLine.visibility = View.VISIBLE
        binding.inclToolbarColor.colorMainLine.text = color.colorName

        binding.inclToolbarColor.colorTopLine.visibility = View.VISIBLE
        val topLineText = color.colorHex
        binding.inclToolbarColor.colorTopLine.text = topLineText

        binding.inclToolbarColor.colorBottomLine.visibility = View.VISIBLE
        binding.inclToolbarColor.colorBottomLine.text = color.toRGBString()

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

    private fun showProgress(show: Boolean) {
        if (show) {
            hideToolbarColor()
            binding.inclToolbarCameraBottom.toolbarCameraButton.visibility = View.GONE
            binding.inclToolbarCameraBottom.toolbarProgressBar.visibility = View.VISIBLE
        } else {
            binding.inclToolbarCameraBottom.toolbarProgressBar.visibility = View.GONE
            binding.inclToolbarCameraBottom.toolbarCameraButton.visibility = View.VISIBLE
        }
    }

}
