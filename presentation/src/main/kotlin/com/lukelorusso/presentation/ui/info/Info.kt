package com.lukelorusso.presentation.ui.info

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.dp
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
                    InfoItem(
                        isEven = true,
                        iconPainter = painterResource(id = R.drawable.info_home),
                        text = stringResource(id = R.string.info_home),
                        onClick = viewModel::gotoHome
                    )
                }

                item {
                    InfoItem(
                        isEven = false,
                        iconPainter = painterResource(id = R.drawable.info_help),
                        text = stringResource(id = R.string.info_help),
                        onClick = viewModel::gotoHelp
                    )
                }

                item {
                    InfoItem(
                        isEven = true,
                        iconPainter = painterResource(id = R.drawable.info_about_me),
                        text = stringResource(id = R.string.info_about_me),
                        onClick = viewModel::gotoAboutMe
                    )
                }

                item {
                    InfoItem(
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
