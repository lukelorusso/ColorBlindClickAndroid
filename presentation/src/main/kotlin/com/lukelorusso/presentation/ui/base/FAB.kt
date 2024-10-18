package com.lukelorusso.presentation.ui.base

import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

const val FAB_SIZE = 96

@Composable
fun BoxScope.FAB(
    alignment: Alignment = Alignment.BottomEnd,
    size: Dp = FAB_SIZE.dp,
    painter: Painter,
    onClick: () -> Unit
) {
    FloatingActionButton(
        modifier = Modifier
            .align(alignment)
            .size(size)
            .padding(16.dp),
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier
                .size(40.dp),
            painter = painter,
            tint = Color.White,
            contentDescription = null
        )
    }
}
