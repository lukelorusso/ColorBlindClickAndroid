package com.lukelorusso.presentation.ui.capture

import androidx.compose.foundation.layout.*
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.lukelorusso.presentation.R

@Composable
internal fun CaptureZoomHandler(
    zoomLevel: Float,
    onLevelChange: (Float) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1f))

        /**
         * This is a VerticalSlider, meaning a 270° rotated Slider
         */
        Slider(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(top = 136.dp, bottom = 224.dp)
                .graphicsLayer {
                    rotationZ = 270f
                    transformOrigin = TransformOrigin(0f, 0f)
                }
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(
                        Constraints(
                            minWidth = constraints.minHeight,
                            maxWidth = constraints.maxHeight,
                            minHeight = constraints.minWidth,
                            maxHeight = constraints.maxHeight,
                        )
                    )
                    layout(placeable.height, placeable.width) {
                        placeable.place(-placeable.width, 0)
                    }
                }
                .fillMaxWidth() // the actual height!
                .height(50.dp), // the actual width!
            value = zoomLevel,
            onValueChange = onLevelChange,
            colors = SliderDefaults.colors(
                thumbColor = colorResource(id = R.color.color_primary),
                activeTrackColor = colorResource(id = R.color.color_accent),
                inactiveTrackColor = colorResource(id = R.color.white_FFFFFF),
            )
        )
    }
}
