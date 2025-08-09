package com.lukelorusso.presentation.ui.history

import androidx.annotation.ColorRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.extensions.getLocalizedDateTime
import com.lukelorusso.presentation.extensions.parseToColor
import com.lukelorusso.domain.model.Color as ColorModel


@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun ColorLine(
    isLoading: Boolean,
    isEven: Boolean,
    item: ColorModel,
    onClick: (ColorModel) -> Unit,
    onDeleteColor: (ColorModel) -> Unit
) {
    @ColorRes val colorRes = if (isEven)
        R.color.item_background_evens
    else
        R.color.item_background_odds
    val context = LocalContext.current
    val dismissDirections = setOf(
        DismissDirection.EndToStart,
        //DismissDirection.StartToEnd
    )
    val dismissValues = setOf(
        DismissValue.DismissedToStart,
        //DismissValue.DismissedToEnd
    )
    val swipeToDismissState = rememberDismissState(
        confirmStateChange = { dismissValue ->
            if (dismissValues.contains(dismissValue)) onDeleteColor(item)
            true
        }
    )

    SwipeToDeleteRow(
        directions = dismissDirections,
        state = swipeToDismissState,
        isLoading = isLoading
    ) {
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
                    drawCircle(color = item.originalColorHex().parseToColor())
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
            }

            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = colorResource(id = R.color.text_color),
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = item.originalColorHex()
            )
        }
    }
}
