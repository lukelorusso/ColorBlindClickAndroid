package com.lukelorusso.presentation.ui.imagepicker

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImagePainter.State.Loading
import coil.compose.rememberAsyncImagePainter
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.error.ErrorMessageFactory
import com.lukelorusso.presentation.extensions.getCentralPixelHash
import com.lukelorusso.presentation.ui.base.CaptureBottomToolbar
import com.lukelorusso.zoomableimagebox.ui.view.ZoomableImageBox
import com.smarttoolfactory.screenshot.*
import kotlinx.coroutines.delay

private const val DELAY_IN_MILLIS = 100L


@OptIn(ExperimentalCoilApi::class)
@Composable
internal fun ImagePicker(
    uri: Uri,
    viewModel: ImagePickerViewModel,
    errorMessageFactory: ErrorMessageFactory
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isCapturing by remember { mutableStateOf(false) }
    var isGestureDetected by remember { mutableStateOf(false) }
    var isPainterLoading by remember { mutableStateOf(false) }
    var resetKey by remember { mutableIntStateOf(0) }
    val painter = rememberAsyncImagePainter(
        model = uri,
        onState = { state -> isPainterLoading = state is Loading }
    )
    val screenshotState = rememberScreenshotState(DELAY_IN_MILLIS)
    val imageResult = screenshotState.imageState.value

    LaunchedEffect(isCapturing) {
        if (isCapturing) {
            delay(DELAY_IN_MILLIS)
            screenshotState.capture()
        }
    }

    LaunchedEffect(imageResult) {
        isCapturing = false
        when (imageResult) {
            is ImageResult.Success ->
                screenshotState.bitmap?.let { bitmap ->
                    val hash = bitmap
                        .getCentralPixelHash(viewModel.uiState.value.pixelNeighbourhood)
                    viewModel.decodeColor(hash)
                }

            is ImageResult.Error ->
                viewModel.setError(imageResult.exception)

            else -> {}
        }
    }

    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.fragment_background)),
            contentAlignment = Alignment.Center
        ) {
            /**
             * The [key] is a workaround to trigger the recomposition of the manipulator
             */
            key(resetKey) {
                ImageManipulator(
                    painter = painter,
                    screenshotState = screenshotState,
                    onGestureDetected = { isGestureDetected = true }
                )
            }

            if (!isCapturing) Icon(
                painter = painterResource(id = R.drawable.viewfinder),
                contentDescription = null
            )

            val onRightButtonSelected: (() -> Unit)? = if (isGestureDetected) ({
                resetKey++
                isGestureDetected = false
            }) else null

            CaptureBottomToolbar(
                showShutterButton = true,
                color = uiState.color,
                errorMessage = uiState.contentState.error?.let(errorMessageFactory::getLocalizedMessage),
                isLoading = isCapturing || isPainterLoading || uiState.contentState.isLoading,
                rightButtonImageVector = Icons.Default.Refresh,
                onRightButtonSelected = onRightButtonSelected,
                onShutterSelected = { isCapturing = true },
                onPreviewSelected = viewModel::gotoPreview
            )
        }
    }
}

@Composable
private fun ImageManipulator(
    painter: Painter,
    screenshotState: ScreenshotState,
    onGestureDetected: () -> Unit
) {
    ScreenshotBox(
        modifier = Modifier
            .fillMaxSize(),
        screenshotState = screenshotState
    ) {
        ZoomableImageBox(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            painter = painter,
            shouldRotate = true,
            showResetIconButton = false,
            onGestureDataChanged = { if (it.isGestureDetected) onGestureDetected() }
        )
    }
}
