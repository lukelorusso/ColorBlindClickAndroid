package com.lukelorusso.colorblindclick.presentation.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lukelorusso.colorblindclick.presentation.R
import com.lukelorusso.colorblindclick.presentation.ui.icons.Clear


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
            .background(MaterialTheme.colorScheme.primary),
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
                contentDescription = null,
                tint = Color.White
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
                        contentDescription = null,
                        tint = Color.White
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
                contentDescription = null,
                tint = Color.White
            )
        }

        val lineModifier = Modifier
            .fillMaxWidth()
            .height(2.dp)

        if (isLoading) LinearProgressIndicator(
            modifier = lineModifier,
            trackColor = colorResource(id = R.color.progress_background),
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
                        imageVector = Clear,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                cursorColor = colorResource(id = R.color.white_50),
                focusedIndicatorColor = Color.White,
                focusedTextColor = Color.White,
                focusedPlaceholderColor = colorResource(id = R.color.white_50),
                focusedLeadingIconColor = Color.Red,
                focusedContainerColor = colorResource(id = R.color.color_primary_dark),
                unfocusedIndicatorColor = colorResource(id = R.color.white_50),
                unfocusedTextColor = Color.White,
                unfocusedPlaceholderColor = colorResource(id = R.color.white_50),
                unfocusedLeadingIconColor = Color.Red,
                unfocusedContainerColor = colorResource(id = R.color.color_primary_dark)
            ),
            shape = RoundedCornerShape(8.dp)
        )
    }
}
