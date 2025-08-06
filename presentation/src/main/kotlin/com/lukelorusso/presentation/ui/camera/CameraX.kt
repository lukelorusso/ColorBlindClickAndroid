package com.lukelorusso.presentation.ui.camera

import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.error.ErrorMessageFactory
import com.lukelorusso.presentation.extensions.*
import com.lukelorusso.domain.model.Color as ColorModel

@Composable
fun CameraX(
    viewModel: CameraViewModel,
    errorMessageFactory: ErrorMessageFactory
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val imageCaptureUseCase = remember { ImageCapture.Builder().build() }
    var previewView by remember { mutableStateOf<PreviewView?>(null) }

    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.fragment_background)),
            contentAlignment = Alignment.Center
        ) {
            CameraPreview(
                lensPosition = uiState.lastLensPosition,
                zoomValue = uiState.lastZoomValue,
                imageCaptureUseCase = imageCaptureUseCase,
                onPreviewReady = { preview ->
                    previewView = preview
                }
            )

            Icon(
                painter = painterResource(id = R.drawable.viewfinder),
                contentDescription = null
            )

            TopToolbar(
                isNextCameraAvailable = true,
                isNextCameraFront = true,
                isFlashAvailable = true,
                onNextCameraSelected = {},
                onFlashSelected = {}
            )

            BottomToolbar(
                colorModel = uiState.color,
                errorMessage = uiState.contentState.error?.let(errorMessageFactory::getLocalizedMessage),
                isLoading = uiState.contentState.isLoading,
                onInfoSelected = viewModel::gotoInfo,
                onShutterSelected = {
                    previewView?.bitmap?.let { bitmap ->
                        val pixel = bitmap.getAveragePixel(viewModel.uiState.value.pixelNeighbourhood)
                        viewModel.decodeColor(pixel.pixelColorToHash())
                    }
                },
                onHistorySelected = viewModel::gotoHistory,
                gotoPreview = viewModel::gotoPreview
            )
        }
    }
}

@Composable
fun CameraPreview(
    lensPosition: Int?,
    zoomValue: Int?, // between 0 and 100
    imageCaptureUseCase: ImageCapture,
    onPreviewReady: (PreviewView) -> Unit
) {
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
    val previewUseCase = remember { Preview.Builder().build() }
    var camera by remember { mutableStateOf<Camera?>(null) }
    var cameraControl by remember { mutableStateOf<CameraControl?>(null) }
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }

    fun rebindCameraProvider() {
        cameraProvider?.let { cameraProvider ->
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(
                localContext as LifecycleOwner,
                cameraSelector,
                previewUseCase,
                imageCaptureUseCase
            )
            cameraControl = camera?.cameraControl
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
        modifier = Modifier
            .fillMaxSize(),
        factory = { context ->
            PreviewView(context).run {
                previewUseCase.surfaceProvider = surfaceProvider
                rebindCameraProvider()
                onPreviewReady(this)
                this
            }
        }
    )
}

@Composable
fun TopToolbar(
    isNextCameraAvailable: Boolean,
    isNextCameraFront: Boolean,
    isFlashAvailable: Boolean,
    onNextCameraSelected: () -> Unit,
    onFlashSelected: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding()
                .fillMaxWidth()
                .background(colorResource(id = R.color.black_50)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val commonPadding = 10.dp

            if (isNextCameraAvailable) {
                IconButton(
                    modifier = Modifier
                        .padding(commonPadding),
                    onClick = onNextCameraSelected
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isNextCameraFront) R.drawable.camera_front_white
                            else R.drawable.camera_rear_white
                        ),
                        contentDescription = null
                    )
                }
            }

            if (isFlashAvailable) {
                IconButton(
                    modifier = Modifier
                        .padding(commonPadding),
                    onClick = onFlashSelected
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.flash_on_white),
                        contentDescription = null
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun BottomToolbar(
    colorModel: ColorModel?,
    errorMessage: String?,
    isLoading: Boolean,
    onInfoSelected: () -> Unit,
    onShutterSelected: () -> Unit,
    onHistorySelected: () -> Unit,
    gotoPreview: (ColorModel) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(1f))

        AnimatedVisibility(
            visible = colorModel != null || errorMessage != null
        ) {
            val clickCallback = {
                if (colorModel != null) gotoPreview(colorModel)
            }
            ResultToolbar(
                textLine1 = colorModel?.colorName,
                textLine2 = colorModel?.originalColorHex() ?: errorMessage,
                textLine3 = colorModel?.toRGBPercentString(),
                color = colorModel?.originalColorHex()?.parseToColor(),
                onTextClick = clickCallback,
                onColorClick = clickCallback
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 85.dp)
                .background(colorResource(id = R.color.black_50)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier
                    .padding(10.dp),
                onClick = onInfoSelected
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.info_outline_white),
                    contentDescription = null
                )
            }

            val commonSize = 80.dp
            val commonPadding = 5.dp
            if (isLoading) {
                val differenceInDp = 5.dp
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(commonSize - differenceInDp)
                        .padding(commonPadding + differenceInDp),
                    color = colorResource(id = R.color.color_accent)
                )
            } else {
                IconButton(
                    modifier = Modifier
                        .size(commonSize)
                        .padding(commonPadding),
                    onClick = onShutterSelected
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.camera_big_white),
                        contentDescription = null
                    )
                }
            }

            IconButton(
                modifier = Modifier
                    .padding(10.dp),
                onClick = onHistorySelected
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.history_big_white),
                    contentDescription = null
                )
            }
        }
    }
}


@Composable
private fun ResultToolbar(
    textLine1: String?,
    textLine2: String?,
    textLine3: String?,
    color: Color?,
    onTextClick: () -> Unit,
    onColorClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = colorResource(id = R.color.black_50),
                    shape = RoundedCornerShape(16.dp, 16.dp)
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            textLine1?.let {
                Text(
                    modifier = Modifier
                        .clickable(onClick = onTextClick),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = it
                )

                Spacer(modifier = Modifier.height(2.dp))
            }

            textLine2?.let {
                Text(
                    modifier = Modifier
                        .clickable(onClick = onTextClick),
                    color = Color.White,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = it
                )

                Spacer(modifier = Modifier.height(2.dp))
            }

            textLine3?.let {
                Text(
                    modifier = Modifier
                        .clickable(onClick = onTextClick),
                    color = Color.White,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = it
                )
            }
        }

        color?.let {
            Canvas(
                modifier = Modifier
                    .padding(10.dp)
                    .size(dimensionResource(id = R.dimen.color_picker_dimens))
                    .border(
                        2.dp,
                        colorResource(id = R.color.text_color),
                        CircleShape
                    )
                    .clip(CircleShape)
                    .clickable(onClick = onColorClick),
                onDraw = {
                    drawRect(color = it)
                }
            )
        }
    }
}
