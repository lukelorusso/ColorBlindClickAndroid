package com.lukelorusso.presentation.ui.capture

import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.IntSize
import com.lukelorusso.presentation.R


@Composable
internal fun ZoomHandler(
    screenIntSize: IntSize,
    zoomLevel: Float,
    onLevelChange: (Float) -> Unit
) {
    Slider(
        modifier = Modifier
            .graphicsLayer(
                scaleX = 1.1f,
                scaleY = 1.1f,
                rotationZ = 270f,
                translationX = screenIntSize.width / 2.5f,
                translationY = screenIntSize.height / 14f * -1
            ),
        value = zoomLevel,
        onValueChange = onLevelChange,
        colors = SliderDefaults.colors(
            thumbColor = colorResource(id = R.color.color_primary),
            activeTrackColor = colorResource(id = R.color.color_accent),
            inactiveTrackColor = colorResource(id = R.color.white_FFFFFF),
        )
    )
}
