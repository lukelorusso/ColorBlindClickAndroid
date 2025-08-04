package com.lukelorusso.presentation.ui.camera

import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraSelector.DEFAULT_FRONT_CAMERA
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.error.ErrorMessageFactory
import androidx.camera.view.PreviewView

@Composable
fun CameraX(
    viewModel: CameraViewModel,
    errorMessageFactory: ErrorMessageFactory
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val imageCaptureUseCase = remember { ImageCapture.Builder().build() }

    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.fragment_background))
        ) {
            CameraPreview(
                lensPosition = uiState.lastLensPosition,
                zoomValue = uiState.lastZoomValue,
                imageCaptureUseCase = imageCaptureUseCase
            )
        }
    }
}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    lensPosition: Int?,
    zoomValue: Int?,
    imageCaptureUseCase: ImageCapture
) {
    val previewUseCase = remember { Preview.Builder().build() }

    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var cameraControl by remember { mutableStateOf<CameraControl?>(null) }

    val localContext = LocalContext.current
    val lensFacing: Int = when (lensPosition) {
        0 -> CameraSelector.LENS_FACING_BACK
        1 -> CameraSelector.LENS_FACING_FRONT
        else -> CameraSelector.LENS_FACING_UNKNOWN
    }
    val zoomLevel: Float = when {
        zoomValue == null || zoomValue < 0 -> 0.1f
        zoomValue >= 100 -> 1f
        else -> zoomValue / 100f
    }

    fun rebindCameraProvider() {
        cameraProvider?.let { cameraProvider ->
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()
            cameraProvider.unbindAll()
            val camera = cameraProvider.bindToLifecycle(
                localContext as LifecycleOwner,
                cameraSelector,
                previewUseCase, imageCaptureUseCase
            )
            cameraControl = camera.cameraControl
        }
    }

    LaunchedEffect(Unit) {
        cameraProvider = ProcessCameraProvider.awaitInstance(localContext)
        rebindCameraProvider()
    }

    LaunchedEffect(lensFacing) {
        rebindCameraProvider()
    }

    LaunchedEffect(zoomLevel) {
        cameraControl?.setLinearZoom(zoomLevel)
    }

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            PreviewView(context).also {
                previewUseCase.surfaceProvider = it.surfaceProvider
                rebindCameraProvider()
            }
        }
    )
}
