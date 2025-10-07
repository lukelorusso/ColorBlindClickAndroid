package com.lukelorusso.presentation.ui.imagepicker

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImagePainter.State.Loading
import coil.compose.rememberAsyncImagePainter
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.ui.base.FAB
import com.lukelorusso.presentation.ui.base.FAB_DEFAULT_SIZE
import com.lukelorusso.zoomableimagebox.ui.view.ZoomableImageBox


@OptIn(ExperimentalCoilApi::class)
@Composable
internal fun ImagePicker(uri: Uri) {
    var isLoading by remember { mutableStateOf(false) }
    var showResetButton by remember { mutableStateOf(false) }
    var resetKey by remember { mutableIntStateOf(0) }
    val painter = rememberAsyncImagePainter(
        model = uri,
        onState = { state -> isLoading = state is Loading }
    )

    Surface {
        /**
         * The [key] is a workaround to trigger the recomposition of the manipulator
         */
        key(resetKey) {
            ImageManipulator(
                painter = painter,
                onGestureDetected = { showResetButton = true }
            )
        }

        if (isLoading) Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(60.dp)
                    .padding(15.dp)
            )
        }

        if (showResetButton) FAB(
            modifier = Modifier
                .padding(16.dp)
                .size(FAB_DEFAULT_SIZE.dp),
            painter = painterResource(id = R.drawable.camera_white),
            onClick = {
                resetKey++
                showResetButton = false
            }
        )
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
