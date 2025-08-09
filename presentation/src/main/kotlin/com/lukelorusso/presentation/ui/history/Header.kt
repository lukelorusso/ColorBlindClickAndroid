package com.lukelorusso.presentation.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lukelorusso.presentation.R


@Composable
internal fun Header(
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
