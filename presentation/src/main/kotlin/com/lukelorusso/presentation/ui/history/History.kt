package com.lukelorusso.presentation.ui.history

import androidx.annotation.ColorRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.error.ErrorMessageFactory
import com.lukelorusso.presentation.extensions.*
import com.lukelorusso.presentation.ui.base.FAB
import com.lukelorusso.presentation.ui.base.FAB_SIZE
import com.lukelorusso.presentation.ui.error.ErrorAlertDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.lukelorusso.domain.model.Color as ColorModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun History(
    viewModel: HistoryViewModel,
    errorMessageFactory: ErrorMessageFactory
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filteredColors = uiState.colorList.filter { item ->
        item.toString().matchSearch(uiState.searchText)
    }
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
        DeleteAlertDialog(
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

        DeleteAlertDialog(
            text = stringResource(R.string.color_delete_one_confirmation_message),
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

@Composable
private fun Header(
    isLoading: Boolean,
    colorListNotEmpty: Boolean,
    isSearchingMode: Boolean,
    searchText: String,
    focusRequester: FocusRequester,
    updateSearchText: (String) -> Unit,
    toggleSearchingMode: () -> Unit,
    onDeleteAllClick: () -> Unit
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = topPadding)
                .requiredHeight(80.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (colorListNotEmpty) Icon(
                modifier = Modifier
                    .padding(16.dp)
                    .size(34.dp)
                    .clickable(onClick = toggleSearchingMode),
                painter = painterResource(id = R.drawable.search_white),
                tint = Color.White,
                contentDescription = null
            )

            Row(
                modifier = Modifier
                    .weight(1F)
                    .run {
                        if (!isSearchingMode && colorListNotEmpty) clickable(onClick = toggleSearchingMode)
                        else this
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isSearchingMode) {
                    SearchTextField(
                        searchText = searchText,
                        updateSearchText = updateSearchText,
                        focusRequester = focusRequester
                    )

                } else {
                    Icon(
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .size(34.dp),
                        painter = painterResource(id = R.drawable.history_white),
                        tint = Color.White,
                        contentDescription = null
                    )

                    Text(
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = stringResource(id = R.string.color_history)
                    )
                }
            }

            if (colorListNotEmpty) Icon(
                modifier = Modifier
                    .padding(16.dp)
                    .size(34.dp)
                    .clickable(onClick = onDeleteAllClick),
                painter = painterResource(id = R.drawable.delete_sweep_white),
                tint = Color.White,
                contentDescription = null
            )
        }

        val lineModifier = Modifier
            .fillMaxWidth()
            .height(2.dp)

        if (isLoading) LinearProgressIndicator(
            modifier = lineModifier,
            backgroundColor = colorResource(id = R.color.progress_background),
            color = colorResource(id = R.color.red_delete)
        )
        else Spacer(
            modifier = lineModifier
                .background(colorResource(id = R.color.fragment_separation_view))
        )
    }
}

@Composable
private fun SearchTextField(
    searchText: String,
    updateSearchText: (String) -> Unit,
    focusRequester: FocusRequester
) {
    val textSelectionColors = TextSelectionColors(
        handleColor = Color.White,
        backgroundColor = colorResource(id = R.color.white_50)
    )

    CompositionLocalProvider(LocalTextSelectionColors provides textSelectionColors) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = searchText,
            onValueChange = updateSearchText,
            singleLine = true,
            placeholder = {
                Text(
                    text = stringResource(R.string.history_search_hint)
                )
            },
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    Icon(
                        modifier = Modifier.clickable { updateSearchText("") },
                        imageVector = Icons.Default.Clear,
                        tint = Color.White,
                        contentDescription = null
                    )
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = colorResource(id = R.color.white_50),
                focusedIndicatorColor = colorResource(id = R.color.white_50),
                textColor = Color.White,
                placeholderColor = colorResource(id = R.color.white_50),
                leadingIconColor = Color.Red
            ),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ColorLine(
    isLoading: Boolean,
    isEven: Boolean,
    item: ColorModel,
    onClick: (ColorModel) -> Unit,
    onDeleteColor: (ColorModel) -> Unit
) {
    @ColorRes val colorRes = if (isEven)
        R.color.item_background_evens
    else
        R.color.item_background_odds
    val context = LocalContext.current
    val dismissDirections = setOf(
        DismissDirection.EndToStart,
        //DismissDirection.StartToEnd
    )
    val dismissValues = setOf(
        DismissValue.DismissedToStart,
        //DismissValue.DismissedToEnd
    )
    val swipeToDismissState = rememberDismissState(
        confirmStateChange = { dismissValue ->
            if (dismissValues.contains(dismissValue)) onDeleteColor(item)
            true
        }
    )

    SwipeToDeleteRow(
        directions = dismissDirections,
        state = swipeToDismissState,
        isLoading = isLoading
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = colorRes))
                .clickable(onClick = { onClick(item) }),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Canvas(
                modifier = Modifier
                    .padding(16.dp)
                    .size(dimensionResource(id = R.dimen.color_picker_dimens))
                    .border(
                        2.dp,
                        colorResource(id = R.color.text_color),
                        CircleShape
                    ),
                onDraw = {
                    drawCircle(color = item.originalColorHex().parseToColor())
                }
            )

            Column(
                modifier = Modifier
                    .weight(1F)
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    color = colorResource(id = R.color.text_color),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = item.colorName
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    color = colorResource(id = R.color.text_color),
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = context.getLocalizedDateTime(item.timestamp)
                )
            }

            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = colorResource(id = R.color.text_color),
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = item.originalColorHex()
            )
        }
    }
}
