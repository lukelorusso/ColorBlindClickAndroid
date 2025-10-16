package com.lukelorusso.presentation.ui.capture

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.ui.base.Bouncer

private const val BOUNCE_DELAY_IN_MILLIS = 250L


@Composable
internal fun CaptureZoomHandler(
    state: MutableFloatState,
    minState: Float = 0F,
    maxState: Float = 100F,
    onRatioChanged: (Float) -> Unit = {},
    onStateChanged: () -> Unit = {}
) {
    var showSlider by remember { mutableStateOf(true) }
    var bouncer by remember { mutableStateOf(Bouncer(BOUNCE_DELAY_IN_MILLIS)) }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, _, gestureZoom, _ ->
                    showSlider = false
                    onRatioChanged(gestureZoom)
                    bouncer.bounce { showSlider = true }
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1F))

        /**
         * This is a VerticalSlider, meaning a 270° rotated Slider
         */
        if (showSlider) Slider(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(top = 136.dp, bottom = 224.dp)
                .graphicsLayer {
                    rotationZ = 270F
                    transformOrigin = TransformOrigin(0F, 0F)
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
            value = state.floatValue,
            valueRange = minState..maxState,
            onValueChange = { state.floatValue = it },
            onValueChangeFinished = onStateChanged,
            colors = SliderDefaults.colors(
                thumbColor = colorResource(id = R.color.color_primary),
                activeTrackColor = colorResource(id = R.color.color_accent),
                inactiveTrackColor = colorResource(id = R.color.white_FFFFFF),
            )
        )
    }
}
