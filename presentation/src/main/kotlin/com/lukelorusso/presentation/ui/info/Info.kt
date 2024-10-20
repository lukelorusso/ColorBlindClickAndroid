package com.lukelorusso.presentation.ui.info

import androidx.annotation.ColorRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.error.ErrorMessageFactory
import com.lukelorusso.presentation.ui.base.FAB
import com.lukelorusso.presentation.ui.base.FAB_SIZE
import com.lukelorusso.presentation.ui.error.ErrorAlertDialog

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Info(
    viewModel: InfoViewModel,
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

    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.fragment_background))
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                stickyHeader {
                    Header(versionName = uiState.versionName)
                }

                item {
                    InfoLine(
                        isEven = true,
                        iconPainter = painterResource(id = R.drawable.info_home),
                        text = stringResource(id = R.string.info_home),
                        onClick = viewModel::gotoHome
                    )
                }

                item {
                    InfoLine(
                        isEven = false,
                        iconPainter = painterResource(id = R.drawable.info_help),
                        text = stringResource(id = R.string.info_help),
                        onClick = viewModel::gotoHelp
                    )
                }

                item {
                    InfoLine(
                        isEven = true,
                        iconPainter = painterResource(id = R.drawable.info_about_me),
                        text = stringResource(id = R.string.info_about_me),
                        onClick = viewModel::gotoAboutMe
                    )
                }

                item {
                    InfoLine(
                        isEven = false,
                        iconPainter = painterResource(id = R.drawable.info_settings),
                        text = stringResource(id = R.string.info_settings),
                        onClick = viewModel::gotoSettings
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(FAB_SIZE.dp))
                }
            }

            FAB(
                painter = painterResource(id = R.drawable.camera_white),
                onClick = viewModel::gotoCamera
            )
        }
    }
}

@Composable
private fun Header(
    versionName: String
) {
    val topPadding = with(LocalDensity.current) {
        WindowInsets.statusBars.getTop(this).toDp()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .padding(
                    top = topPadding + 24.dp,
                    bottom = 24.dp
                )
                .size(64.dp),
            painter = painterResource(id = R.drawable.ic_launcher_splash),
            contentDescription = null
        )

        Row(
            modifier = Modifier
                .padding(bottom = 20.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                modifier = Modifier.padding(bottom = 2.dp),
                color = Color.White,
                fontSize = 18.sp,
                text = stringResource(id = R.string.app_name)
            )

            Text(
                modifier = Modifier.padding(start = 5.dp),
                color = Color.White,
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic,
                text = versionName
            )
        }

        val lineModifier = Modifier
            .fillMaxWidth()
            .height(2.dp)

        Spacer(
            modifier = lineModifier
                .background(colorResource(id = R.color.fragment_separation_view))
        )
    }
}

@Composable
private fun InfoLine(
    isEven: Boolean,
    iconPainter: Painter,
    text: String,
    onClick: () -> Unit
) {
    @ColorRes val colorRes = if (isEven)
        R.color.item_background_evens
    else
        R.color.item_background_odds

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = colorRes))
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .padding(16.dp)
                .size(34.dp),
            painter = iconPainter,
            tint = colorResource(id = R.color.text_color),
            contentDescription = null
        )

        Text(
            modifier = Modifier
                .weight(1F)
                .padding(vertical = 16.dp),
            color = colorResource(id = R.color.text_color),
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = text
        )

        Icon(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .size(34.dp),
            painter = painterResource(id = R.drawable.keyboard_arrow_right),
            tint = colorResource(id = R.color.text_color),
            contentDescription = null
        )
    }
}
