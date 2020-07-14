package com.lukelorusso.presentation.scenes.camera

import android.content.pm.PackageManager
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.jakewharton.rxbinding4.view.clicks
import com.lukelorusso.data.helper.TimberWrapper
import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.extensions.*
import com.lukelorusso.presentation.helper.TrackerHelper
import com.lukelorusso.presentation.scenes.base.view.ABaseFragment
import com.lukelorusso.presentation.scenes.base.view.LoadingState
import com.lukelorusso.presentation.scenes.main.MainActivity
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration
import io.fotoapparat.log.Logger
import io.fotoapparat.parameter.Flash
import io.fotoapparat.selector.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.layout_camera_toolbar_bottom.*
import kotlinx.android.synthetic.main.layout_camera_toolbar_top.*
import kotlinx.android.synthetic.main.layout_color_toolbar.*
import timber.log.Timber
import javax.inject.Inject

class CameraFragment : ABaseFragment(R.layout.fragment_camera), CameraView {

    companion object {
        val TAG: String = CameraFragment::class.java.simpleName
        private const val INIT_ZOOM_VALUE = 10
        private const val MAX_ZOOM_VALUE = 100

        fun newInstance(): CameraFragment = CameraFragment()
    }

    @Inject
    lateinit var presenter: CameraPresenter

    @Inject
    lateinit var trackerHelper: TrackerHelper

    // Intents
    private val intentGetColor = PublishSubject.create<Pair<String, String>>()
    private val intentSetLastLensPosition = PublishSubject.create<Int>()
    private val intentOpenPreview = PublishSubject.create<Color>()

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
                hideColorPanel()
                true
            }
            else -> false
        }
    }

    override fun onStop() {
        super.onStop()
        camera?.stop()
    }

    override fun onResume() {
        super.onResume()
        presenter.attach(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.detach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        toolbarCameraButton.setOnClickListener {
            showProgress(true)
            camera?.also { camera ->
                camera.takePicture().toBitmap().whenAvailable { bitmapPhoto ->
                    bitmapPhoto?.also { result ->
                        Handler().post {
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

        toolbarSwitchCameraButton.setOnClickListener {
            camera?.also { camera ->
                camera.switchTo(
                    lensPosition = if (isFrontCamera) back() else front(),
                    cameraConfiguration = CameraConfiguration()
                )
                isFrontCamera = !isFrontCamera
                intentSetLastLensPosition.onNext(if (isFrontCamera) 1 else 0)
                initToolbarTop()
                checkCameraCapabilities()
                camera.setZoom(cameraZoomSeekBar.progress.toFloat().div(MAX_ZOOM_VALUE))
            }
        }

        toolbarFlashButton.setOnClickListener {
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
    }

    private fun initToolbarTop(isFlashOn: Boolean = false) {
        toolbarSwitchCameraButton.setImageResource(
            if (isFrontCamera) R.drawable.camera_rear_white
            else R.drawable.camera_front_white
        )
        toolbarFlashButton.setImageResource(
            if (isFlashOn) R.drawable.flash_off_white
            else R.drawable.flash_on_white
        )
    }

    private fun checkFrontCamera() {
        if (requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            toolbarCameraTop?.visibility = View.VISIBLE
            toolbarSwitchCameraButton?.visibility = View.VISIBLE
        }
    }

    private fun checkCameraCapabilities() {
        Handler().post {
            camera?.also { camera ->
                camera.getCapabilities().whenAvailable {
                    if (it?.flashModes?.contains(Flash.On) == true) {
                        toolbarCameraTop?.visibility = View.VISIBLE
                        toolbarFlashButton?.visibility = View.VISIBLE
                    } else {
                        toolbarFlashButton?.visibility = View.GONE
                    }

                    if (it?.zoom.toString() == "Zoom.FixedZoom") {
                        cameraZoomSeekBar?.visibility = View.GONE
                    } else {
                        cameraZoomSeekBar?.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    // region INTENTS
    override fun intentLoadData(): Observable<Unit> = Observable.just(Unit)

    override fun intentSetLastLensPosition(): Observable<Int> = intentSetLastLensPosition

    override fun intentGotoInfo(): Observable<Unit> = toolbarInfoButton.clicks().map { Unit }

    override fun intentGotoHistory(): Observable<Unit> = toolbarHistoryButton.clicks().map { Unit }

    override fun intentGetColor(): Observable<Pair<String, String>> = intentGetColor

    override fun intentOpenPreview(): Observable<Color> = intentOpenPreview
    // endregion

    // region RENDER
    override fun render(viewModel: CameraViewModel) {
        TimberWrapper.d { "render: $viewModel" }

        activity?.runOnUiThread {
            showProgress(
                viewModel.loadingState == LoadingState.LOADING ||
                        viewModel.loadingState == LoadingState.RETRY
            )
            renderHomeUrl(viewModel.homeUrl)
            renderInitCamera(viewModel.lastLensPosition)
            renderColorResult(viewModel.color)
            showToolbarColor(viewModel.errorMessage)
            showToolbarColor(viewModel.snackMessage)
            renderPersistenceException(viewModel.isPersistenceException)
        }
    }

    private fun renderHomeUrl(homeUrl: String?) {
        homeUrl?.also { this.homeUrl = it }
    }

    private fun renderInitCamera(lastLensPosition: Int?) {
        lastLensPosition?.also { position ->
            camera = Fotoapparat(
                context = requireContext(),
                view = cameraView,
                focusView = focusView,
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

            cameraZoomSeekBar?.maxValue = MAX_ZOOM_VALUE
            cameraZoomSeekBar?.setOnProgressChangeListener { progressValue ->
                camera?.setZoom(progressValue.toFloat().div(MAX_ZOOM_VALUE))
            }
            cameraZoomSeekBar?.progress = INIT_ZOOM_VALUE
            cameraZoomSeekBar?.visibility = View.VISIBLE

            val duration = resources.getInteger(R.integer.fading_effect_duration_default)
            Handler().postDelayed({
                (activity as? MainActivity)?.hideSplashScreen()
            }, duration.toLong())
        }
    }

    private fun renderColorResult(color: Color?) {
        color?.also { result -> showToolbarColor(result) }
    }

    private fun renderPersistenceException(isPersistenceException: Boolean?) {
        if (isPersistenceException == true)
            trackerHelper.track(activity, TrackerHelper.Actions.PERSISTENCE_EXCEPTION)
    }
    // endregion

    private fun showToolbarColor(color: Color) {
        toolbarColor.fadeInView()

        colorPreviewPanel.visibility = View.VISIBLE

        (colorPreviewPanel.background as? GradientDrawable)?.setColor(color.colorHex.hashColorToPixel())

        colorMainLine.visibility = View.VISIBLE
        colorMainLine.text = color.colorName

        colorTopLine.visibility = View.VISIBLE
        val topLineText = color.colorHex
        colorTopLine.text = topLineText

        colorBottomLine.visibility = View.VISIBLE
        colorBottomLine.text = color.toRGBString()

        toolbarColor.setOnClickListener { intentOpenPreview.onNext(color) }
    }

    private fun showToolbarColor(errorMessage: String?) {
        errorMessage?.also { msg ->
            toolbarColor.fadeInView()

            colorPreviewPanel.visibility = View.GONE

            colorTopLine.visibility = View.VISIBLE
            colorTopLine.text = msg

            colorMainLine.visibility = View.GONE

            colorBottomLine.visibility = View.GONE

            toolbarColor.setOnClickListener(null)
        }
    }

    private fun showProgress(show: Boolean) {
        if (show) {
            hideColorPanel()
            toolbarCameraButton.visibility = View.GONE
            toolbarProgressBar.visibility = View.VISIBLE
        } else {
            toolbarProgressBar.visibility = View.GONE
            toolbarCameraButton.visibility = View.VISIBLE
        }
    }

    private fun hideColorPanel() {
        if (isToolbarColorVisible()) {
            val duration = resources.getInteger(R.integer.fading_effect_duration_fast)
            toolbarColor.fadeOutView(duration)
        }
    }

    private fun isToolbarColorVisible(): Boolean {
        return toolbarColor.visibility == View.VISIBLE
    }

}
