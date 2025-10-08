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
import com.lukelorusso.presentation.ui.base.BottomSheetUpperLine
import com.lukelorusso.presentation.ui.error.ErrorAlertDialog

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

            uiState.color?.let { colorModel ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    val color = colorModel.originalColorHex().parseToColor()

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
                            drawRect(color = colorModel.originalColorHex().parseToColor())
                        }
                    )

                    val description = (stringResource(id = R.string.credits) + " " + uiState.storeUrl).let { credits ->
                        colorModel.sharableDescription(credits)
                    }
                    val textPopupLabel = stringResource(id = R.string.choose_an_app)
                    val bitmapPopupLabel = stringResource(id = R.string.share_color)
                    val size = with(LocalDensity.current) {
                        dimensionResource(id = R.dimen.color_picker_dimens_big).roundToPx()
                    }

                    BottomToolbar(
                        colorModel = colorModel,
                        onTextClick = {
                            viewModel.shareText(
                                text = description,
                                popupLabel = textPopupLabel
                            )
                        },
                        onColorClick = {
                            viewModel.shareBitmap(
                                bitmap = createBitmap(
                                    size,
                                    size,
                                    color.toArgb()
                                ),
                                description = description,
                                popupLabel = bitmapPopupLabel
                            )
                        }
                    )
                }
            }
        }
    }
}
