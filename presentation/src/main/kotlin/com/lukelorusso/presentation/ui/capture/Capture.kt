package com.lukelorusso.presentation.ui.capture

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.lukelorusso.presentation.logger.TimberLogger
import com.lukelorusso.presentation.ui.base.*
import com.lukelorusso.presentation.ui.imagepicker.ImagePickerActivity
import kotlin.math.roundToInt

val ICON_BUTTON_PADDING = 5.dp
val ICON_BUTTON_SIZE = 62.dp
val ICON_SIZE = 40.dp
private const val INIT_ZOOM_VALUE = 10F // when no previous value has been saved (fresh installation)

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
    var showPhotoPickerFAB by remember { mutableStateOf(true) }
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { imageUri ->
            TimberLogger.d { "Image selected -> $imageUri" }
            showPhotoPickerFAB = true
            imageUri?.let { uri ->
                val intent = ImagePickerActivity.newIntent(context, uri)
                context.startActivity(intent)
            }
        }
    )
    var zoomRatio by remember { mutableFloatStateOf(1F) }
    var zoomValue by remember { mutableFloatStateOf(-1F) } // @FloatRange(from = 0F, to = 100F)

    LaunchedEffect(uiState.lastZoomValue) {
        uiState.lastZoomValue?.also { value ->
            val lastZoomValue = value.toFloat()

            when {
                lastZoomValue < 0 ->
                    zoomValue = INIT_ZOOM_VALUE

                zoomValue != lastZoomValue ->
                    zoomValue = lastZoomValue
            }
        }
    }

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

            CaptureCameraPreview(
                lensFacing = lensFacing,
                zoomRatio = zoomRatio,
                linearZoom = zoomValue / 100,
                onCameraPreviewReady = { cameraReady, previewViewReady ->
                    cameraReady?.let { camera = it }
                    previewViewReady?.let {
                        previewView = it
                        LifecycleCameraController(context).run {
                            bindToLifecycle(context as LifecycleOwner)
                            it.controller = this
                        }
                    }
                },
                onLinearZoomChanged = {
                    val newZoomValue = (it * 100).roundToInt()
                    viewModel.setLastZoomValue(newZoomValue)
                }
            )

            Icon(
                painter = painterResource(id = R.drawable.viewfinder),
                contentDescription = null,
                tint = Color.White
            )

            CaptureZoomHandler(
                zoomValue = zoomValue,
                onValueChanged = {
                    zoomValue = it
                    val newZoomValue = it.roundToInt()
                    viewModel.setLastZoomValue(newZoomValue)
                },
                onRatioChanged = { newRatio -> zoomRatio = newRatio }
            )

            if (previewView?.controller?.initializationFuture?.isDone == true) {
                val torchState = camera?.cameraInfo?.torchState?.observeAsState()
                val isFlashOn = torchState?.value == TorchState.ON
                val isNextCameraFront = lensFacing == CameraSelector.LENS_FACING_BACK

                CaptureTopToolbar(
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

            CaptureBottomToolbar(
                showShutterButton = screenIntSize != IntSize.Zero,
                color = uiState.color,
                errorMessage = uiState.contentState.error?.let(errorMessageFactory::getLocalizedMessage),
                isLoading = uiState.contentState.isLoading,
                leftButtonPainter = painterResource(id = R.drawable.info_outline_white),
                onLeftButtonSelected = viewModel::gotoInfo,
                rightButtonPainter = painterResource(id = R.drawable.history_big_white),
                onRightButtonSelected = viewModel::gotoHistory,
                onShutterSelected = {
                    previewView?.bitmap?.let { bitmap ->
                        val hash = bitmap
                            .getCentralPixelHash(viewModel.uiState.value.pixelNeighbourhood)
                        viewModel.decodeColor(hash)
                    }
                },
                onPreviewSelected = viewModel::gotoPreview
            )

            if (showPhotoPickerFAB) FAB(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(horizontal = 16.dp)
                    .padding(top = 136.dp)
                    .size(FAB_DEFAULT_SIZE.dp),
                painter = painterResource(id = R.drawable.gallery_white),
                onClick = {
                    singlePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                    showPhotoPickerFAB = false
                }
            )
        }
    }
}
