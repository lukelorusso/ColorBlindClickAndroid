package com.lukelorusso.presentation.ui.capture

import androidx.camera.core.*
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
    onCameraPreviewReady: (Camera?, PreviewView?) -> Unit,
    onLinearZoomChanged: (Float) -> Unit // @FloatRange(from = 0F, to = 1F)
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val previewUseCase = remember { Preview.Builder().build() }
    var camera by remember { mutableStateOf<Camera?>(null) }
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }

    fun currentZoomRatio() =
        camera?.cameraInfo?.zoomState?.value?.zoomRatio ?: 1F

    fun currentLinearZoom() =
        camera?.cameraInfo?.zoomState?.value?.linearZoom

    fun rebindCameraProvider() {
        cameraProvider?.let { cameraProvider ->
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()
            camera = cameraProvider.bindToLifecycle(
                context as LifecycleOwner,
                cameraSelector,
                previewUseCase
            )
            onCameraPreviewReady(camera, null)
        }
    }

    LifecycleStartEffect(Lifecycle.Event.ON_RESUME) {
        coroutineScope.launch {
            if (cameraProvider == null) cameraProvider = ProcessCameraProvider.awaitInstance(context)
            rebindCameraProvider()
        }

        onStopOrDispose {
            cameraProvider?.unbindAll()
        }
    }

    LaunchedEffect(lensFacing) {
        cameraProvider?.unbindAll()
        rebindCameraProvider()
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
                rebindCameraProvider()
                onCameraPreviewReady(null, this)
                this
            }
        }
    )
}
