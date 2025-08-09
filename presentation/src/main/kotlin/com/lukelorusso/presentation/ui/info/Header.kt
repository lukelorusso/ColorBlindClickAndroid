package com.lukelorusso.presentation.ui.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lukelorusso.presentation.R


@Composable
internal fun Header(
    versionName: String
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
        Image(
            modifier = Modifier
                .padding(
                    top = topPadding + 24.dp,
                    bottom = 24.dp
                )
                .size(64.dp),
            painter = painterResource(id = R.drawable.ic_launcher_splash),
            contentDescription = null
        )

        Row(
            modifier = Modifier
                .padding(bottom = 20.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                modifier = Modifier.padding(bottom = 2.dp),
                color = Color.White,
                fontSize = 18.sp,
                text = stringResource(id = R.string.app_name)
            )

            Text(
                modifier = Modifier.padding(start = 5.dp),
                color = Color.White,
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic,
                text = versionName
            )
        }

        val lineModifier = Modifier
            .fillMaxWidth()
            .height(2.dp)

        Spacer(
            modifier = lineModifier
                .background(colorResource(id = R.color.fragment_separation_view))
        )
    }
}
