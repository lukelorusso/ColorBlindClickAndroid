package com.lukelorusso.presentation.ui.base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.dp
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.extensions.*
import com.lukelorusso.presentation.ui.capture.*
import com.lukelorusso.domain.model.Color as ColorEntity


@Composable
internal fun CaptureBottomToolbar(
    showShutterButton: Boolean = true,
    color: ColorEntity?,
    errorMessage: String?,
    isLoading: Boolean,
    leftButtonPainter: Painter? = null,
    leftButtonImageVector: ImageVector? = null,
    onLeftButtonSelected: (() -> Unit)? = null,
    rightButtonPainter: Painter? = null,
    rightButtonImageVector: ImageVector? = null,
    onRightButtonSelected: (() -> Unit)? = null,
    onShutterSelected: () -> Unit,
    onPreviewSelected: (ColorEntity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(1F))

        AnimatedVisibility(
            visible = color != null || errorMessage != null
        ) {
            val clickCallback = {
                if (color != null) onPreviewSelected(color)
            }
            ResultToolbar(
                textLine1 = color?.colorName,
                textLine2 = color
                    ?.originalColorHex()
                    ?.hashColorToRGBDecimal()
                    ?.closestHtmlColor()
                    ?.toHtmlColorString(),
                textLine3 = color
                    ?.toDetailedString()
                    ?: errorMessage,
                contentAlignment = Alignment.CenterEnd,
                onTextClick = clickCallback
            ) {
                color?.originalColorHex()?.parseToColor()?.let {
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
                            .clickable(onClick = clickCallback),
                        onDraw = {
                            drawRect(color = it)
                        }
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 85.dp)
                .background(colorResource(id = R.color.black_50)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val sideIconButtonModifier = Modifier
                .padding(ICON_BUTTON_PADDING)
                .size(ICON_BUTTON_SIZE)

            val sideIconModifier = Modifier
                .size(ICON_SIZE)

            onLeftButtonSelected?.let { callback ->
                IconButton(
                    modifier = sideIconButtonModifier,
                    onClick = callback
                ) {
                    leftButtonPainter?.let { painter ->
                        Icon(
                            modifier = sideIconModifier,
                            painter = painter,
                            contentDescription = null,
                            tint = Color.White
                        )
                    } ?: leftButtonImageVector?.let { imageVector ->
                        Icon(
                            modifier = sideIconModifier,
                            imageVector = imageVector,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            } ?: Spacer(modifier = sideIconButtonModifier)

            val shutterSize = 60.dp
            val shutterPadding = 5.dp
            if (isLoading) {
                val differenceInDp = 6.dp
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(shutterPadding)
                        .size(shutterSize - differenceInDp),
                    color = colorResource(id = R.color.color_accent)
                )
            } else {
                AnimatedVisibility(showShutterButton) {
                    IconButton(
                        modifier = Modifier
                            .padding(shutterPadding)
                            .size(shutterSize),
                        onClick = onShutterSelected
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.camera_big_white),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }

            onRightButtonSelected?.let { callback ->
                IconButton(
                    modifier = sideIconButtonModifier,
                    onClick = callback
                ) {
                    rightButtonPainter?.let { painter ->
                        Icon(
                            modifier = sideIconModifier,
                            painter = painter,
                            contentDescription = null,
                            tint = Color.White
                        )
                    } ?: rightButtonImageVector?.let { imageVector ->
                        Icon(
                            modifier = sideIconModifier,
                            imageVector = imageVector,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            } ?: Spacer(modifier = sideIconButtonModifier)
        }
    }
}

/**
 * HTML: Maroon
 */
@Composable
fun HtmlColor.toHtmlColorString(): String =
    "HTML: " + stringResource(this.resId())

/**
 * #5D362F, rgb(36.47%, 21.18%, 18.43%)
 */
fun ColorEntity.toDetailedString(): String {
    val originalColorHex = this.originalColorHex()
    return "$originalColorHex, " + originalColorHex
        .hashColorToRGBDecimal()
        .toRGBPercent()
        .let { "rgb(${it.first}%, ${it.second}%, ${it.third}%)" }
}
