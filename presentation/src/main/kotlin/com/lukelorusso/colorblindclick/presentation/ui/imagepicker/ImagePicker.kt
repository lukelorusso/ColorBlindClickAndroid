package com.lukelorusso.colorblindclick.presentation.ui.imagepicker

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImagePainter.State.Loading
import coil.compose.rememberAsyncImagePainter
import com.lukelorusso.colorblindclick.presentation.R
import com.lukelorusso.colorblindclick.presentation.error.ErrorMessageFactory
import com.lukelorusso.colorblindclick.presentation.extensions.getCentralPixelHash
import com.lukelorusso.colorblindclick.presentation.ui.base.CaptureBottomToolbar
import com.lukelorusso.colorblindclick.presentation.ui.icons.Refresh
import com.lukelorusso.zoomableimagebox.ui.view.ZoomableImageBox
import com.smarttoolfactory.screenshot.ImageResult
import com.smarttoolfactory.screenshot.ScreenshotBox
import com.smarttoolfactory.screenshot.ScreenshotState
import com.smarttoolfactory.screenshot.rememberScreenshotState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

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
            delay(DELAY_IN_MILLIS.milliseconds)
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
                contentDescription = null,
                tint = Color.White
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
                rightButtonImageVector = Refresh,
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
                .background(MaterialTheme.colorScheme.background),
            painter = painter,
            shouldRotate = false,
            showResetIconButton = false,
            onGestureDataChanged = { if (it.isGestureDetected) onGestureDetected() }
        )
    }
}
