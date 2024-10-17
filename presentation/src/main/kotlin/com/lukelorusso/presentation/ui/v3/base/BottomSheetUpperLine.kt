package com.lukelorusso.presentation.ui.v3.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.lukelorusso.presentation.R

private const val TOP_LINE_HEIGHT = 3
private const val TOP_LINE_WIDTH = 32
private const val TOP_LINE_PADDING = 10

@Composable
fun BottomSheetUpperLine(
    modifier: Modifier = Modifier
) = Box(
    modifier = modifier
        .padding(TOP_LINE_PADDING.dp)
        .height(TOP_LINE_HEIGHT.dp)
        .width(TOP_LINE_WIDTH.dp)
        .background(colorResource(id = R.color.dialog_line))
)
