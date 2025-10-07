package com.lukelorusso.presentation.ui.imagepicker

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImagePainter.State.Loading
import coil.compose.rememberAsyncImagePainter
import com.lukelorusso.zoomableimagebox.ui.view.ZoomableImageBox


@OptIn(ExperimentalCoilApi::class)
@Composable
internal fun ImagePicker(uri: Uri) {
    var isLoading by remember { mutableStateOf(false) }
    val painter = rememberAsyncImagePainter(
        model = uri,
        onState = { state -> isLoading = state is Loading }
    )

    Surface {
        ZoomableImageBox(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            painter = painter,
            shouldRotate = true
        )

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
    }
}
