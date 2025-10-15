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
import kotlinx.coroutines.launch


@Composable
internal fun CameraPreview(
    lensFacing: Int,
    zoomLevel: Float?,
    onCameraPreviewReady: (Camera?, PreviewView?) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val previewUseCase = remember { Preview.Builder().build() }
    var cameraControl by remember { mutableStateOf<CameraControl?>(null) }
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }

    fun rebindCameraProvider() {
        cameraProvider?.let { cameraProvider ->
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()
            val camera = cameraProvider.bindToLifecycle(
                context as LifecycleOwner,
                cameraSelector,
                previewUseCase
            )
            cameraControl = camera.cameraControl
            zoomLevel?.let { level -> cameraControl?.setLinearZoom(level) }
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

    LaunchedEffect(zoomLevel) {
        zoomLevel?.let { level -> cameraControl?.setLinearZoom(level) }
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
