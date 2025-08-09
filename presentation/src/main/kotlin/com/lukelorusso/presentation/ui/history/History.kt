package com.lukelorusso.presentation.ui.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.error.ErrorMessageFactory
import com.lukelorusso.presentation.ui.base.*
import com.lukelorusso.presentation.ui.error.ErrorAlertDialog
import kotlinx.coroutines.*
import com.lukelorusso.domain.model.Color as ColorModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun History(
    viewModel: HistoryViewModel,
    errorMessageFactory: ErrorMessageFactory
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filteredColors by viewModel.filteredColors.collectAsState(
        initial = emptyList(),
        context = Dispatchers.IO
    )
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    var localSearchText by remember { mutableStateOf("") }
    var showDeleteAllAlertDialog by remember { mutableStateOf(false) }
    var shouldDeleteColor by remember { mutableStateOf<ColorModel?>(null) }

    // use this function instead of playing directly with the viewModel
    fun updateSearch(
        isSearchingMode: Boolean = viewModel.uiState.value.isSearchingMode,
        newText: String = viewModel.uiState.value.searchText
    ) {
        viewModel.toggleSearchingMode(isSearchingMode)

        if (isSearchingMode) {
            localSearchText = newText
            viewModel.updateSearchText(newText)
        } else {
            localSearchText = ""
            viewModel.updateSearchText("")
        }
    }

    // request focus on SearchTextField
    if (uiState.run { uiState.isSearchingMode && !contentState.isLoading && searchText.isEmpty() }) {
        DisposableEffect(Unit) {
            coroutineScope.launch {
                delay(100L)
                focusRequester.requestFocus()
            }
            onDispose { }

        }
    }

    if (uiState.contentState.isError) {
        ErrorAlertDialog(
            message = uiState.contentState.error
                ?.let { errorMessageFactory.getLocalizedMessage(it) },
            dismissCallback = viewModel::dismissError
        )
    }

    if (showDeleteAllAlertDialog) {
        YesNoAlertDialog(
            text = stringResource(R.string.color_delete_all_confirmation_message),
            painter = painterResource(id = R.drawable.delete_sweep_white),
            confirmCallback = {
                viewModel.deleteAllColors()
                showDeleteAllAlertDialog = false
            },
            dismissCallback = { showDeleteAllAlertDialog = false }
        )
    }

    shouldDeleteColor?.let { colorToDelete ->
        if (uiState.colorList.isEmpty() && uiState.isSearchingMode) {
            updateSearch(isSearchingMode = false)
        }

        YesNoAlertDialog(
            text = stringResource(R.string.color_delete_one_confirmation_message, colorToDelete.colorName),
            painter = painterResource(id = R.drawable.delete_item_white),
            confirmCallback = {
                viewModel.deleteColor(colorToDelete)
                shouldDeleteColor = null
            },
            dismissCallback = {
                viewModel.restoreColorToUiState(colorToDelete)
                shouldDeleteColor = null
            }
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
                    Header(
                        isLoading = uiState.contentState.isLoading,
                        colorListNotEmpty = uiState.colorList.isNotEmpty(),
                        isSearchingMode = uiState.isSearchingMode,
                        searchText = localSearchText,
                        focusRequester = focusRequester,
                        updateSearchText = { updateSearch(newText = it) },
                        toggleSearchingMode = {
                            updateSearch(isSearchingMode = !uiState.isSearchingMode)
                        },
                        onDeleteAllClick = {
                            updateSearch(isSearchingMode = false)
                            showDeleteAllAlertDialog = true
                        }
                    )
                }

                if (uiState.colorList.isEmpty()) item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        color = colorResource(id = R.color.text_color),
                        fontSize = 18.sp,
                        text = stringResource(id = R.string.history_no_item),
                        textAlign = TextAlign.Center
                    )
                }

                itemsIndexed(
                    items = filteredColors,
                    key = { _, colorModel -> colorModel.timestamp } // setting a key will solve graphical glitches on SwipeToDismiss
                ) { index, colorModel ->
                    ColorLine(
                        isLoading = uiState.contentState.isLoading,
                        isEven = index % 2 == 0,
                        item = colorModel,
                        onClick = viewModel::gotoPreview,
                        onDeleteColor = {
                            viewModel.deleteColorFromUiState(colorModel)
                            shouldDeleteColor = colorModel
                        }
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
