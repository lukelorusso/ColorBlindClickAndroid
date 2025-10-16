package com.smarttoolfactory.screenshot

import android.graphics.Bitmap
import android.os.Build
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalView

/**
 * Original source: https://github.com/SmartToolFactory/Compose-Screenshot
 *
 * A composable that gets screenshot of Composable that is in [content].
 * @param screenshotState state of screenshot that contains [Bitmap].
 * @param content Composable that will be captured to bitmap on action or periodically.
 */
@Composable
fun ScreenshotBox(
    modifier: Modifier = Modifier,
    screenshotState: ScreenshotState,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit
) {
    var composableBounds by remember { mutableStateOf<Rect?>(null) }
    val view: View = LocalView.current

    DisposableEffect(Unit) {
        screenshotState.callback = {
            composableBounds?.let { bounds ->
                if (bounds.width == 0F || bounds.height == 0F) return@let

                view.screenshot(bounds) { imageResult: ImageResult ->
                    screenshotState.imageState.value = imageResult

                    if (imageResult is ImageResult.Success) {
                        screenshotState.bitmapState.value = imageResult.data
                    }
                }
            }
        }

        onDispose {
            val bmp = screenshotState.bitmapState.value
            bmp?.apply {
                if (!isRecycled) {
                    recycle()
                }
            }
            screenshotState.bitmapState.value = null
            screenshotState.callback = null
        }
    }

    Box(
        modifier = modifier
            .onGloballyPositioned {
                composableBounds = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    it.boundsInWindow()
                } else {
                    it.boundsInRoot()
                }
            },
        contentAlignment = contentAlignment,
        content = content
    )
}
