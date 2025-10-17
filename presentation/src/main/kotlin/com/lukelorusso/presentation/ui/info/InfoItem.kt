package com.lukelorusso.presentation.ui.info

import androidx.annotation.ColorRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lukelorusso.presentation.R


@Composable
internal fun InfoItem(
    isEven: Boolean,
    iconPainter: Painter,
    text: String,
    onClick: () -> Unit
) {
    @ColorRes val colorRes = if (isEven)
        R.color.item_background_evens
    else
        R.color.item_background_odds

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = colorRes))
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .padding(16.dp)
                .size(34.dp),
            painter = iconPainter,
            contentDescription = null,
            tint = colorResource(id = R.color.text_color)
        )

        Text(
            modifier = Modifier
                .weight(1F)
                .padding(vertical = 16.dp),
            color = colorResource(id = R.color.text_color),
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = text
        )

        Icon(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .size(34.dp),
            painter = painterResource(id = R.drawable.keyboard_arrow_right),
            contentDescription = null,
            tint = colorResource(id = R.color.text_color)
        )
    }
}
