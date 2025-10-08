package com.lukelorusso.presentation.ui.preview

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.extensions.toRGBPercentString
import com.lukelorusso.presentation.ui.base.FAB
import com.lukelorusso.presentation.ui.base.FAB_DEFAULT_SIZE
import com.lukelorusso.domain.model.Color as ColorEntity


@Composable
internal fun BottomToolbar(
    color: ColorEntity,
    onTextClick: () -> Unit,
    onColorClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
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
            Text(
                modifier = Modifier
                    .clickable(onClick = onTextClick),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.W500,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = color.colorName
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                modifier = Modifier
                    .clickable(onClick = onTextClick),
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = color.originalColorHex()
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                modifier = Modifier
                    .clickable(onClick = onTextClick),
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = color.toRGBPercentString()
            )
        }

        FAB(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(FAB_DEFAULT_SIZE.dp),
            painter = painterResource(id = R.drawable.share_white),
            onClick = onColorClick
        )
    }
}
