package com.lukelorusso.presentation.ui.base

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lukelorusso.presentation.R

@Composable
fun ResultToolbar(
    modifier: Modifier = Modifier,
    textLine1: String?,
    textLine2: String?,
    textLine3: String?,
    contentAlignment: Alignment = Alignment.TopStart,
    onTextClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = contentAlignment
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = colorResource(id = R.color.black_50),
                    shape = RoundedCornerShape(16.dp, 16.dp)
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            textLine1?.let {
                Text(
                    modifier = Modifier
                        .clickable(onClick = onTextClick),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = it
                )

                Spacer(modifier = Modifier.height(2.dp))
            }

            textLine2?.let {
                Text(
                    modifier = Modifier
                        .clickable(onClick = onTextClick),
                    color = Color.White,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = it
                )

                Spacer(modifier = Modifier.height(2.dp))
            }

            textLine3?.let {
                Text(
                    modifier = Modifier
                        .clickable(onClick = onTextClick),
                    color = Color.White,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = it
                )
            }
        }

        content()
    }
}
