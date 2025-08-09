package com.lukelorusso.presentation.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WarningAmber
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
import com.lukelorusso.presentation.error.ErrorMessageFactory
import com.lukelorusso.presentation.ui.base.*
import com.lukelorusso.presentation.ui.error.ErrorAlertDialog

private val viewfinderPixelsValueStringResList = listOf(
    R.string.settings_viewfinder_pixels_value_0,
    R.string.settings_viewfinder_pixels_value_1,
    R.string.settings_viewfinder_pixels_value_2
)

@Composable
fun Settings(
    viewModel: SettingsViewModel,
    errorMessageFactory: ErrorMessageFactory
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showViewfinderMultiOption by remember { mutableStateOf(false) }
    var showCameraOptionWarningAlertDialog by remember { mutableStateOf(false) }

    if (uiState.contentState.isError) {
        ErrorAlertDialog(
            message = uiState.contentState.error
                ?.let { errorMessageFactory.getLocalizedMessage(it) },
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

    if (showCameraOptionWarningAlertDialog) {
        YesNoAlertDialog(
            text = stringResource(R.string.settings_save_camera_options_off_confirmation_message),
            imageVector = Icons.Default.WarningAmber,
            tint = colorResource(id = R.color.yellow_warning),
            confirmCallback = {
                viewModel.setSaveCameraOptions(false)
                showCameraOptionWarningAlertDialog = false
            },
            dismissCallback = { showCameraOptionWarningAlertDialog = false }
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
                    fun setSaveCameraOption(newValue: Boolean) {
                        if (newValue) {
                            viewModel.setSaveCameraOptions(true)
                        } else {
                            showCameraOptionWarningAlertDialog = true
                        }
                    }

                    SettingRow(
                        text = stringResource(id = R.string.settings_save_camera_options),
                        onClick = { setSaveCameraOption(!uiState.saveCameraOptions) }
                    ) {
                        Switch(
                            modifier = Modifier
                                .scale(1.5F)
                                .padding(24.dp),
                            checked = uiState.saveCameraOptions,
                            onCheckedChange = ::setSaveCameraOption
                        )
                    }
                }
            }
        }
    }
}
