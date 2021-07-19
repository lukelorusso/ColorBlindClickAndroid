package com.lukelorusso.presentation.ui.camera

import android.content.pm.PackageManager
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.usecase.GetColorUseCase
import com.lukelorusso.domain.usecase.base.Logger
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.databinding.FragmentCameraBinding
import com.lukelorusso.presentation.extensions.*
import com.lukelorusso.presentation.helper.TrackerHelper
import com.lukelorusso.presentation.ui.base.ARenderFragment
import com.lukelorusso.presentation.ui.base.ContentState
import com.lukelorusso.presentation.ui.main.MainActivity
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration
import io.fotoapparat.parameter.Flash
import io.fotoapparat.selector.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import io.fotoapparat.log.Logger as FotoApparatLogger

class CameraFragment : ARenderFragment<CameraData>(R.layout.fragment_camera) {

    companion object {
        val TAG: String = CameraFragment::class.java.simpleName
        private const val INIT_ZOOM_VALUE = 10
        private const val MAX_ZOOM_VALUE = 100

        fun newInstance(): CameraFragment = CameraFragment()
    }

    // Intents
    private val intentGetColor = PublishSubject.create<GetColorUseCase.Param>()

    private val intentGetLastZoomValue = PublishSubject.create<Unit>()
    private val intentSetLastZoomValue = PublishSubject.create<Int>()
    private val intentSetLastLensPosition = PublishSubject.create<Int>()

    // View
    private val binding by viewBinding(FragmentCameraBinding::bind)
    private val viewModel by viewModel<CameraViewModel>()
    private val logger by inject<Logger>()

    // Properties
    private val trackerHelper by inject<TrackerHelper>()
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
            isActive() && isToolbarColorVisible() -> {
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
        viewModel.initRouter(
            requireActivity() as AppCompatActivity,
            fragment = this
        ) // If there's a router, initialize it here
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        viewModel.observe(
            viewLifecycleOwner,
            dataObserver = { data -> data?.let(::render) },
            eventObserver = { event -> event?.let(::renderEvent) }
        )

        if (savedInstanceState != null) {
            val duration = resources.getInteger(R.integer.splash_screen_duration)
            Handler(Looper.getMainLooper())
                .postDelayed({ intentGetLastZoomValue.onNext(Unit) }, duration.toLong())
        }
    }

    // region RENDER
    override fun render(data: CameraData) {
        showLoading(
            show = data.contentState == ContentState.LOADING ||
                    data.contentState == ContentState.RETRY
        )

        renderHomeUrl(data.homeUrl)
        renderInitCamera(data.lastLensPosition, data.lastZoomValue)
        renderColorResult(data.color)
        //renderError(binding.inclError.textErrorDescription, data.errorMessage)
        renderPersistenceException(data.isPersistenceException)
    }

    override fun renderSnack(messageError: String?) {
        showToolbarColor(messageError)
    }

    private fun renderHomeUrl(homeUrl: String?) {
        homeUrl?.also { this.homeUrl = it }
    }

    private fun renderInitCamera(lastLensPosition: Int?, lastZoomValue: Int?) {
        lastLensPosition?.also { position ->
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
                    intentSetLastZoomValue.onNext(progressValue)
                }
                visibility = View.VISIBLE
            }
        }

        lastZoomValue?.also {
            binding.cameraZoomSeekBar.progress = if (it == -1) INIT_ZOOM_VALUE else it
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
            camera?.also { camera ->
                showLoading(true)
                camera.takePicture().toBitmap().whenAvailable { bitmapPhoto ->
                    bitmapPhoto?.also { result ->
                        Handler(Looper.getMainLooper()).post {
                            val bitmap = result.bitmap
                            val pixel = bitmap.getPixel(
                                bitmap.width / 2,
                                bitmap.height / 2
                            )
                            intentGetColor.onNext(
                                GetColorUseCase.Param(
                                    colorHex = pixel.pixelColorToHash(),
                                    deviceUdid = activity.getDeviceUdid()
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
                viewModel.intentGetLastLensPositionAndZoomValue(unit)
            )
        }
        val getColor = intentGetColor
            .flatMap { viewModel.intentGetColor(it) }
        val setLastLensPosition = intentSetLastLensPosition
            .flatMap { viewModel.intentSetLastLensPosition(it) }
        val getLastZoomValue = intentGetLastZoomValue
            .flatMap { viewModel.intentGetLastZoomValue(it) }
        val setLastZoomValue = intentSetLastZoomValue
            .flatMap { viewModel.intentSetLastZoomValue(it) }

        viewModel.subscribe(
            loadData,
            getColor,
            setLastLensPosition,
            getLastZoomValue,
            setLastZoomValue
        )
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

    override fun showLoading(loading: View, visible: Boolean) {
        if (visible) {
            hideToolbarColor()
            binding.inclToolbarCameraBottom.toolbarCameraButton.visibility = View.GONE
            loading.visibility = View.VISIBLE
        } else {
            loading.visibility = View.GONE
            binding.inclToolbarCameraBottom.toolbarCameraButton.visibility = View.VISIBLE
        }
    }

}
