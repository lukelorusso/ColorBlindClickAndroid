package com.lukelorusso.presentation.ui.v3.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.ui.v3.base.BottomSheetUpperLine
import com.lukelorusso.presentation.ui.v3.base.MultiOptionDialog
import com.lukelorusso.presentation.ui.v3.error.ErrorAlertDialog

private val viewfinderPixelsValueStringResList = listOf(
    R.string.settings_viewfinder_pixels_value_0,
    R.string.settings_viewfinder_pixels_value_1,
    R.string.settings_viewfinder_pixels_value_2
)

@Composable
fun Settings(
    viewModel: SettingsViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showViewfinderMultiOption by remember { mutableStateOf(false) }

    if (uiState.contentState.isError) {
        ErrorAlertDialog(
            message = uiState.contentState.errorMessage,
            dismissCallback = viewModel::dismissError
        )
    }

    if (showViewfinderMultiOption) {
        MultiOptionDialog(
            optionLabels = viewfinderPixelsValueStringResList.map { stringResource(id = it) },
            selectedPosition = uiState.pixelNeighbourhood,
            dismissCallback = { showViewfinderMultiOption = false },
            onOptionSelected = viewModel::setPixelNeighbourhood
        )
    }

    Surface(
        modifier = Modifier.nestedScroll(rememberNestedScrollInteropConnection()), // fixes issue scrolling down while inside a BottomSheetDialogFragment
        color = Color.Transparent // AppCardDialogFragment already implements a custom theme
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.dialog_background))
        ) {
            BottomSheetUpperLine(
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    Text(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            top = 64.dp,
                            end = 16.dp,
                            bottom = 32.dp
                        ),
                        color = colorResource(id = R.color.text_color),
                        fontSize = 42.sp,
                        text = stringResource(id = R.string.info_settings)
                    )
                }

                item {
                    SettingColumn(
                        text = stringResource(id = R.string.settings_viewfinder_pixels),
                        onClick = { showViewfinderMultiOption = true }
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 16.dp
                                ),
                            color = colorResource(id = R.color.text_color),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 14.sp,
                            text = stringResource(id = viewfinderPixelsValueStringResList[uiState.pixelNeighbourhood])
                        )
                    }
                }

                item {
                    SettingLine(
                        text = stringResource(id = R.string.settings_save_camera_options),
                        onClick = { viewModel.setSaveCameraOptions(!uiState.saveCameraOptions) }
                    ) {
                        Switch(
                            modifier = Modifier
                                .scale(1.5F)
                                .padding(16.dp),
                            checked = uiState.saveCameraOptions,
                            onCheckedChange = {
                                viewModel.setSaveCameraOptions(!uiState.saveCameraOptions)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingColumn(
    text: String,
    onClick: (() -> Unit)? = null,
    optionContent: @Composable (RowScope.() -> Unit)
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .run {
                if (onClick != null) clickable(onClick = onClick)
                else this
            }
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 2.dp
                ),
            color = colorResource(id = R.color.text_color),
            fontSize = 20.sp,
            text = text
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            optionContent()
        }
    }
}

@Composable
fun SettingLine(
    text: String,
    onClick: (() -> Unit)? = null,
    optionContent: @Composable (RowScope.() -> Unit)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .run {
                if (onClick != null) clickable(onClick = onClick)
                else this
            }
    ) {
        Text(
            modifier = Modifier
                .weight(1.2F)
                .padding(16.dp),
            color = colorResource(id = R.color.text_color),
            fontSize = 20.sp,
            text = text
        )

        Spacer(modifier = Modifier.width(5.dp))

        Row(
            modifier = Modifier
                .weight(0.8F),
            horizontalArrangement = Arrangement.End
        ) {
            optionContent()
        }
    }
}
