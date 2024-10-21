package com.lukelorusso.presentation.ui.preview

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.error.ErrorMessageFactory
import com.lukelorusso.presentation.extensions.*
import com.lukelorusso.presentation.ui.base.BottomSheetUpperLine
import com.lukelorusso.presentation.ui.base.FAB
import com.lukelorusso.presentation.ui.error.ErrorAlertDialog
import com.lukelorusso.domain.model.Color as ColorModel

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
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .padding(64.dp)
                            .border(
                                2.dp,
                                colorResource(id = R.color.text_color),
                                RectangleShape
                            ),
                        onDraw = {
                            drawRect(color = colorModel.originalColorHex().parseToColor())
                        }
                    )

                    val description = (stringResource(id = R.string.credits) + " " + uiState.homeUrl).let { credits ->
                        colorModel.sharableDescription(credits)
                    }
                    val textPopupLabel = stringResource(id = R.string.choose_an_app)
                    val bitmapPopupLabel = stringResource(id = R.string.share_color)
                    val size = with(LocalDensity.current) {
                        dimensionResource(id = R.dimen.color_picker_dimens_big).roundToPx()
                    }

                    BottomToolBar(
                        colorModel = colorModel,
                        shareText = {
                            viewModel.shareText(
                                text = description,
                                popupLabel = textPopupLabel
                            )
                        },
                        shareBitmap = {
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

@Composable
private fun BottomToolBar(
    colorModel: ColorModel,
    shareText: () -> Unit,
    shareBitmap: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
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
            Text(
                modifier = Modifier
                    .clickable(onClick = shareText),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.W500,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = colorModel.colorName
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                modifier = Modifier
                    .clickable(onClick = shareText),
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = colorModel.originalColorHex()
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                modifier = Modifier
                    .clickable(onClick = shareText),
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = colorModel.toRGBPercentString()
            )
        }

        FAB(
            painter = painterResource(id = R.drawable.share_white),
            onClick = shareBitmap
        )
    }
}
