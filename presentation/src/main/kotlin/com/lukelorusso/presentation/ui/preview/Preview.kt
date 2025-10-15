package com.lukelorusso.presentation.ui.preview

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.error.ErrorMessageFactory
import com.lukelorusso.presentation.extensions.*
import com.lukelorusso.presentation.ui.base.*
import com.lukelorusso.presentation.ui.error.ErrorAlertDialog
import com.lukelorusso.domain.model.Color as ColorEntity


private const val LINE_BREAK = "\n"

@Composable
fun Preview(
    viewModel: PreviewViewModel,
    errorMessageFactory: ErrorMessageFactory
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.contentState.isError) {
        ErrorAlertDialog(
            message = uiState.contentState.error
                ?.let { errorMessageFactory.getLocalizedMessage(it) },
            dismissCallback = viewModel::dismissError
        )
    }

    Surface(
        color = Color.Transparent // AppCardDialogFragment already implements a custom theme
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            BottomSheetUpperLine(
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )

            uiState.color?.let { color ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Canvas(
                        modifier = Modifier
                            .padding(64.dp)
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .border(
                                2.dp,
                                colorResource(id = R.color.text_color),
                                RectangleShape
                            ),
                        onDraw = {
                            drawRect(color = color.originalColorHex().parseToColor())
                        }
                    )

                    val description = (stringResource(id = R.string.credits) + " " + uiState.storeUrl).let { credits ->
                        color.toSharableDescription(credits)
                    }
                    val textPopupLabel = stringResource(id = R.string.choose_an_app)
                    val bitmapPopupLabel = stringResource(id = R.string.share_color)
                    val size = with(LocalDensity.current) {
                        dimensionResource(id = R.dimen.color_picker_dimens_big).roundToPx()
                    }
                    val onTextClick: () -> Unit = {
                        viewModel.shareText(
                            text = description,
                            popupLabel = textPopupLabel
                        )
                    }
                    val onColorClick: () -> Unit = {
                        viewModel.shareBitmap(
                            bitmap = createBitmap(
                                size,
                                size,
                                color.originalColorHex().parseToColor().toArgb()
                            ),
                            description = description,
                            popupLabel = bitmapPopupLabel
                        )
                    }

                    ResultToolbar(
                        textLine1 = color.colorName,
                        textLine2 = color
                            .originalColorHex()
                            .hashColorToRGBDecimal()
                            .closestHtmlColor()
                            .toHtmlColorString(),
                        textLine3 = color.toDetailedString(),
                        onTextClick = onTextClick
                    ) {
                        FAB(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp)
                                .size(FAB_DEFAULT_SIZE.dp),
                            painter = painterResource(id = R.drawable.share_white),
                            onClick = onColorClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorEntity.toSharableDescription(credits: String): String = (this.colorName + LINE_BREAK
        + this.originalColorHex().hashColorToRGBDecimal().closestHtmlColor().toHtmlColorString() + LINE_BREAK
        + this.toDetailedString() + LINE_BREAK
        + LINE_BREAK
        + credits)
