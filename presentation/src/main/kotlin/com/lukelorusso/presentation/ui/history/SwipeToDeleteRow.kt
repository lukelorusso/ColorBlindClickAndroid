package com.lukelorusso.presentation.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lukelorusso.presentation.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToDeleteRow(
    directions: Set<DismissDirection>,
    state: DismissState,
    isLoading: Boolean = false,
    dismissContent: @Composable (RowScope.() -> Unit)
) {
    if (isLoading) Row {
        dismissContent()
    }
    else SwipeToDismiss(
        state = state,
        directions = directions,
        dismissThresholds = { FractionalThreshold(.65F) }, // percentage of swipe before dismissing
        background = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorResource(id = R.color.red_delete)),
                horizontalArrangement = when (state.dismissDirection) {
                    DismissDirection.EndToStart ->
                        Arrangement.End

                    DismissDirection.StartToEnd ->
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
        },
        dismissContent = dismissContent
    )
}
