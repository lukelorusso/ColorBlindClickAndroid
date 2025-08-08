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
internal fun SettingRow(
    text: String,
    onClick: (() -> Unit)? = null,
    optionContent: @Composable (RowScope.() -> Unit)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .run {
                if (onClick != null) clickable(onClick = onClick)
                else this
            }
    ) {
        Text(
            modifier = Modifier
                .weight(1.2F)
                .padding(16.dp),
            color = colorResource(id = R.color.text_color),
            fontSize = 20.sp,
            text = text
        )

        Spacer(modifier = Modifier.width(5.dp))

        Row(
            modifier = Modifier
                .weight(0.8F),
            horizontalArrangement = Arrangement.End
        ) {
            optionContent()
        }
    }
}
