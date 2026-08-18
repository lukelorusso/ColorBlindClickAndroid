package com.lukelorusso.colorblindclick.presentation.ui.history

import androidx.annotation.ColorRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lukelorusso.colorblindclick.domain.entity.ColorEntity
import com.lukelorusso.colorblindclick.presentation.R
import com.lukelorusso.colorblindclick.presentation.extensions.getLocalizedDateTime
import com.lukelorusso.colorblindclick.presentation.extensions.parseToColor


@Composable
internal fun ColorLine(
    isEven: Boolean,
    item: ColorEntity,
    onClick: (ColorEntity) -> Unit,
    onDeleteColor: (ColorEntity) -> Unit,
    onEditTag: (ColorEntity) -> Unit
) {
    val swipeToDismissState = rememberSwipeToDismissBoxState(
        positionalThreshold = { totalDistance -> totalDistance * 0.60f } // percentage of swipe before dismissing
    )

    LaunchedEffect(swipeToDismissState.currentValue) {
        if (swipeToDismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onDeleteColor(item)
            swipeToDismissState.snapTo(SwipeToDismissBoxValue.Settled)
        }
    }

    SwipeToDismissBox(
        modifier = Modifier,
        state = swipeToDismissState,
        backgroundContent = { DeletedItemContent(swipeToDismissState) },
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true
    ) {
        ItemContent(
            isEven = isEven,
            item = item,
            onClick = onClick,
            onEditTag = onEditTag
        )
    }
}

@Composable
private fun ItemContent(
    isEven: Boolean,
    item: ColorEntity,
    onClick: (ColorEntity) -> Unit,
    onEditTag: (ColorEntity) -> Unit,
) {
    @ColorRes val colorRes = if (isEven)
        R.color.item_background_evens
    else
        R.color.item_background_odds
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = colorRes))
            .clickable(onClick = { onClick(item) }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(
            modifier = Modifier
                .padding(16.dp)
                .size(dimensionResource(id = R.dimen.color_picker_dimens))
                .border(
                    2.dp,
                    colorResource(id = R.color.text_color),
                    CircleShape
                )
                .clip(CircleShape),
            onDraw = {
                drawCircle(color = item.originalColorHex.parseToColor())
            }
        )

        Column(
            modifier = Modifier
                .weight(1F)
                .padding(vertical = 16.dp)
        ) {
            Text(
                color = colorResource(id = R.color.text_color),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = item.colorName
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                color = colorResource(id = R.color.text_color),
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = context.getLocalizedDateTime(item.timestamp)
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                modifier = Modifier.clickable(onClick = { onEditTag(item) }),
                color = colorResource(id = R.color.text_color),
                fontSize = 13.sp,
                fontStyle = if (item.tag.isNullOrBlank()) FontStyle.Italic else FontStyle.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = item.tag?.takeIf { it.isNotBlank() }
                    ?: stringResource(id = R.string.color_tag_add_placeholder)
            )
        }

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = colorResource(id = R.color.text_color),
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = item.originalColorHex
        )
    }
}

@Composable
private fun DeletedItemContent(swipeToDismissState: SwipeToDismissBoxState) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.red_delete)),
        horizontalArrangement = when (swipeToDismissState.dismissDirection) {
            SwipeToDismissBoxValue.EndToStart ->
                Arrangement.End

            SwipeToDismissBoxValue.StartToEnd ->
                Arrangement.Start

            else ->
                Arrangement.Center
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(horizontal = 16.dp),
            painter = painterResource(id = R.drawable.delete_item_white),
            contentDescription = null,
            tint = Color.White
        )
    }
}
