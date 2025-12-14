package com.lukelorusso.presentation.ui.capture

import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector.Builder
import androidx.camera.core.CameraSelector.LENS_FACING_FRONT
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LifecycleStartEffect
import com.lukelorusso.presentation.logger.TimberLogger
import kotlinx.coroutines.launch


@Composable
internal fun CaptureCameraPreview(
    lensFacing: Int,
    zoomRatio: Float, // the resulting gesture of pinch to zoom
    linearZoom: Float, // @FloatRange(from = 0F, to = 1F)
    switchCameraLens: (Exception) -> Unit,
    onCameraPreviewReady: (Camera?, PreviewView?) -> Unit,
    onLinearZoomChanged: (Float) -> Unit // @FloatRange(from = 0F, to = 1F)
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val previewUseCase = remember { Preview.Builder().build() }
    var camera by remember { mutableStateOf<Camera?>(null) }
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var lastLensFacing by remember { mutableIntStateOf(LENS_FACING_FRONT) }

    fun cameraSelectorBuilder() = Builder()
        .requireLensFacing(lastLensFacing)
        .build()

    fun currentLinearZoom() =
        camera?.cameraInfo?.zoomState?.value?.linearZoom

    fun currentZoomRatio() =
        camera?.cameraInfo?.zoomState?.value?.zoomRatio ?: 1F

    fun unbindCameraProvider() {
        cameraProvider?.unbindAll()
        camera = null
    }

    fun bindCameraProvider() {
        cameraProvider?.let { cameraProvider ->
            val cameraSelector = cameraSelectorBuilder()

            if (cameraProvider.isBound(previewUseCase))
                unbindCameraProvider()

            try {
                camera = cameraProvider.bindToLifecycle(
                    context as LifecycleOwner,
                    cameraSelector,
                    previewUseCase
                )
                onCameraPreviewReady(camera, null)
            } catch (e: IllegalArgumentException) {
                switchCameraLens(e)
            }
        }
    }

    LifecycleStartEffect(Lifecycle.Event.ON_RESUME) {
        coroutineScope.launch {
            if (cameraProvider == null) cameraProvider = ProcessCameraProvider.awaitInstance(context)
            bindCameraProvider()
        }

        onStopOrDispose {
            unbindCameraProvider()
        }
    }

    LaunchedEffect(lensFacing) {
        lastLensFacing = lensFacing
        unbindCameraProvider()
        bindCameraProvider()
    }

    LaunchedEffect(zoomRatio) {
        if (zoomRatio != 1F) {
            TimberLogger.d { "CameraPreview.zoomRatio -> $zoomRatio" }
            val newZoomRatio = currentZoomRatio() * zoomRatio
            camera?.cameraControl?.setZoomRatio(newZoomRatio)
            currentLinearZoom()?.let { onLinearZoomChanged(it) }
        }
    }

    LaunchedEffect(linearZoom) {
        if (linearZoom >= 0) {
            TimberLogger.d { "CameraPreview.linearZoom -> $linearZoom" }
            camera?.cameraControl?.setLinearZoom(linearZoom)
        }
    }

    AndroidView(
        modifier = Modifier
            .fillMaxSize(),
        factory = { context ->
            PreviewView(context).run {
                previewUseCase.surfaceProvider = surfaceProvider
                bindCameraProvider()
                onCameraPreviewReady(null, this)
                this
            }
        }
    )
}
