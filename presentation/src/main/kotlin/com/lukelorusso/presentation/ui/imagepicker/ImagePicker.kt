package com.lukelorusso.presentation.ui.imagepicker

import android.net.Uri
import androidx.compose.foundation.background
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
import com.lukelorusso.presentation.extensions.getCentralColor
import com.lukelorusso.presentation.extensions.pixelColorToHash
import com.lukelorusso.presentation.ui.base.CaptureBottomToolbar
import com.lukelorusso.zoomableimagebox.ui.view.ZoomableImageBox
import com.smarttoolfactory.screenshot.*


@OptIn(ExperimentalCoilApi::class)
@Composable
internal fun ImagePicker(
    uri: Uri,
    viewModel: ImagePickerViewModel,
    errorMessageFactory: ErrorMessageFactory
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isLoading by remember { mutableStateOf(false) }
    var showResetButton by remember { mutableStateOf(false) }
    var resetKey by remember { mutableIntStateOf(0) }
    val painter = rememberAsyncImagePainter(
        model = uri,
        onState = { state -> isLoading = state is Loading }
    )
    val screenshotState = rememberScreenshotState()
    val imageResult = screenshotState.imageState.value

    LaunchedEffect(imageResult) {
        when (imageResult) {
            is ImageResult.Success ->
                screenshotState.bitmap?.let { bitmap ->
                    val pixel = bitmap.getCentralColor(viewModel.uiState.value.pixelNeighbourhood)
                    viewModel.decodeColor(pixel.pixelColorToHash())
                }

            is ImageResult.Error ->
                viewModel.setError(imageResult.exception)

            else -> {}
        }
    }

    Surface {
        ScreenshotBox(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.fragment_background)),
            contentAlignment = Alignment.Center,
            screenshotState = screenshotState
        ) {
            /**
             * The [key] is a workaround to trigger the recomposition of the manipulator
             */
            key(resetKey) {
                ImageManipulator(
                    painter = painter,
                    onGestureDetected = { showResetButton = true }
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.viewfinder),
                contentDescription = null
            )

            val onRightButtonSelected: (() -> Unit)? = if (showResetButton) ({
                resetKey++
                showResetButton = false
            }) else null

            CaptureBottomToolbar(
                showShutterButton = true,
                colorModel = uiState.color,
                errorMessage = uiState.contentState.error?.let(errorMessageFactory::getLocalizedMessage),
                isLoading = uiState.contentState.isLoading || isLoading,
                rightButtonImageVector = Icons.Default.Refresh,
                onRightButtonSelected = onRightButtonSelected,
                onShutterSelected = screenshotState::capture,
                onPreviewSelected = viewModel::gotoPreview
            )
        }
    }
}

@Composable
private fun ImageManipulator(
    painter: Painter,
    onGestureDetected: () -> Unit
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
