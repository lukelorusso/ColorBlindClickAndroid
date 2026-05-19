package com.lukelorusso.colorblindclick.presentation.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lukelorusso.colorblindclick.presentation.R


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
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1F)
                .padding(16.dp),
            color = colorResource(id = R.color.text_color),
            fontSize = 20.sp,
            text = text
        )

        Spacer(modifier = Modifier.width(5.dp))

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.End
        ) {
            optionContent()
        }
    }
}
