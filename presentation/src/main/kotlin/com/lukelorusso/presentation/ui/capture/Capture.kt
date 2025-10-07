package com.lukelorusso.presentation.ui.capture

import androidx.camera.core.*
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.error.ErrorMessageFactory
import com.lukelorusso.presentation.extensions.*
import kotlin.math.roundToInt

val ICON_PADDING = 5.dp
val ICON_SIZE = 62.dp

@Composable
fun Capture(
    viewModel: CaptureViewModel,
    errorMessageFactory: ErrorMessageFactory
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var camera by remember { mutableStateOf<Camera?>(null) }
    var previewView by remember { mutableStateOf<PreviewView?>(null) }
    var screenIntSize by remember { mutableStateOf(IntSize.Zero) }

    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned { coordinates ->
                    screenIntSize = coordinates.size
                }
                .background(colorResource(id = R.color.fragment_background)),
            contentAlignment = Alignment.Center
        ) {
            val lensFacing: Int = when (uiState.lastLensPosition) {
                0 -> CameraSelector.LENS_FACING_BACK
                1 -> CameraSelector.LENS_FACING_FRONT
                else -> CameraSelector.LENS_FACING_UNKNOWN
            }

            val zoomLevel: Float? = uiState.lastZoomValue?.let { zoomValue ->
                when {
                    zoomValue < 0 -> 0.1f
                    zoomValue >= 100 -> 1f
                    else -> zoomValue / 100f
                }
            }

            CameraPreview(
                lensFacing = lensFacing,
                zoomLevel = zoomLevel,
                onCameraPreviewReady = { cameraReady, previewViewReady ->
                    cameraReady?.let { camera = it }
                    previewViewReady?.let {
                        previewView = it
                        LifecycleCameraController(context).run {
                            bindToLifecycle(context as LifecycleOwner)
                            it.controller = this
                        }
                    }
                }
            )

            Icon(
                painter = painterResource(id = R.drawable.viewfinder),
                contentDescription = null
            )

            if (screenIntSize != IntSize.Zero && zoomLevel != null) {
                ZoomHandler(
                    screenIntSize = screenIntSize,
                    zoomLevel = zoomLevel,
                    onLevelChange = { viewModel.setLastZoomValue((it * 100).roundToInt()) }
                )
            }

            if (previewView?.controller?.initializationFuture?.isDone == true) {
                val torchState = camera?.cameraInfo?.torchState?.observeAsState()
                val isFlashOn = torchState?.value == TorchState.ON
                val isNextCameraFront = lensFacing == CameraSelector.LENS_FACING_BACK

                TopToolbar(
                    isNextCameraAvailable = previewView
                        ?.controller
                        ?.run { canSwitchToFront() || canSwitchToBack() }
                        ?: false,
                    isNextCameraFront = isNextCameraFront,
                    isFlashAvailable = camera?.cameraInfo?.hasFlashUnit() == true,
                    isFlashOn = isFlashOn,
                    onNextCameraSelected = {
                        val newPosition = if (isNextCameraFront) 1 else 0
                        viewModel.setLastLensPosition(newPosition)
                    },
                    onFlashSelected = { camera?.cameraControl?.enableTorch(isFlashOn.not()) }
                )
            }

            BottomToolbar(
                screenIntSize = screenIntSize,
                colorModel = uiState.color,
                errorMessage = uiState.contentState.error?.let(errorMessageFactory::getLocalizedMessage),
                isLoading = uiState.contentState.isLoading,
                onInfoSelected = viewModel::gotoInfo,
                onShutterSelected = {
                    previewView?.bitmap?.let { bitmap ->
                        val pixel = bitmap.getCentralColor(viewModel.uiState.value.pixelNeighbourhood)
                        viewModel.decodeColor(pixel.pixelColorToHash())
                    }
                },
                onHistorySelected = viewModel::gotoHistory,
                gotoPreview = viewModel::gotoPreview
            )
        }
    }
}
