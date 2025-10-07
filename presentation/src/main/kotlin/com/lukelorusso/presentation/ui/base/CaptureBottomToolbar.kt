package com.lukelorusso.presentation.ui.base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.extensions.parseToColor
import com.lukelorusso.presentation.extensions.toRGBPercentString
import com.lukelorusso.presentation.ui.capture.ICON_BUTTON_PADDING
import com.lukelorusso.presentation.ui.capture.ICON_BUTTON_SIZE
import com.lukelorusso.presentation.ui.capture.ICON_SIZE
import com.lukelorusso.domain.model.Color as ColorModel


@Composable
internal fun CaptureBottomToolbar(
    showShutterButton: Boolean = true,
    colorModel: ColorModel?,
    errorMessage: String?,
    isLoading: Boolean,
    leftButtonPainter: Painter? = null,
    leftButtonImageVector: ImageVector? = null,
    onLeftButtonSelected: (() -> Unit)? = null,
    rightButtonPainter: Painter? = null,
    rightButtonImageVector: ImageVector? = null,
    onRightButtonSelected: (() -> Unit)? = null,
    onShutterSelected: () -> Unit,
    onPreviewSelected: (ColorModel) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(1f))

        AnimatedVisibility(
            visible = colorModel != null || errorMessage != null
        ) {
            val clickCallback = {
                if (colorModel != null) onPreviewSelected(colorModel)
            }
            ResultToolbar(
                textLine1 = colorModel?.colorName,
                textLine2 = colorModel?.originalColorHex() ?: errorMessage,
                textLine3 = colorModel?.toRGBPercentString(),
                color = colorModel?.originalColorHex()?.parseToColor(),
                onTextClick = clickCallback,
                onColorClick = clickCallback
            )
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
                            contentDescription = null
                        )
                    } ?: leftButtonImageVector?.let { imageVector ->
                        Icon(
                            modifier = sideIconModifier,
                            imageVector = imageVector,
                            contentDescription = null
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
                            contentDescription = null
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
                            contentDescription = null
                        )
                    } ?: rightButtonImageVector?.let { imageVector ->
                        Icon(
                            modifier = sideIconModifier,
                            imageVector = imageVector,
                            contentDescription = null
                        )
                    }
                }
            } ?: Spacer(modifier = sideIconButtonModifier)
        }
    }
}

@Composable
private fun ResultToolbar(
    textLine1: String?,
    textLine2: String?,
    textLine3: String?,
    color: Color?,
    onTextClick: () -> Unit,
    onColorClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = colorResource(id = R.color.black_50),
                    shape = RoundedCornerShape(16.dp, 16.dp)
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            textLine1?.let {
                Text(
                    modifier = Modifier
                        .clickable(onClick = onTextClick),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = it
                )

                Spacer(modifier = Modifier.height(2.dp))
            }

            textLine2?.let {
                Text(
                    modifier = Modifier
                        .clickable(onClick = onTextClick),
                    color = Color.White,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = it
                )

                Spacer(modifier = Modifier.height(2.dp))
            }

            textLine3?.let {
                Text(
                    modifier = Modifier
                        .clickable(onClick = onTextClick),
                    color = Color.White,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = it
                )
            }
        }

        color?.let {
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
                    .clickable(onClick = onColorClick),
                onDraw = {
                    drawRect(color = it)
                }
            )
        }
    }
}
