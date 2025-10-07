package com.lukelorusso.presentation.ui.base

import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

const val FAB_DEFAULT_SIZE = 64

@Composable
fun FAB(
    modifier: Modifier,
    painter: Painter,
    onClick: () -> Unit
) {
    FloatingActionButton(
        modifier = modifier,
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
