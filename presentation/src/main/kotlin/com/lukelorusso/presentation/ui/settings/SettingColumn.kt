package com.lukelorusso.presentation.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lukelorusso.presentation.R


@Composable
internal fun SettingColumn(
    text: String,
    onClick: (() -> Unit)? = null,
    optionContent: @Composable (RowScope.() -> Unit)
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .run {
                if (onClick != null) clickable(onClick = onClick)
                else this
            }
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 2.dp
                ),
            color = colorResource(id = R.color.text_color),
            fontSize = 20.sp,
            text = text
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            optionContent()
        }
    }
}
