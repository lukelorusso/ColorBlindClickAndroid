package com.lukelorusso.colorblindclick.presentation.ui.base

import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.lukelorusso.colorblindclick.presentation.R

const val FAB_DEFAULT_SIZE = 64

@Composable
fun FAB(
    modifier: Modifier,
    painter: Painter,
    onClick: () -> Unit
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        containerColor = colorResource(id = R.color.color_accent),
    ) {
        Icon(
            modifier = Modifier
                .size(40.dp),
            painter = painter,
            contentDescription = null,
            tint = Color.White
        )
    }
}
