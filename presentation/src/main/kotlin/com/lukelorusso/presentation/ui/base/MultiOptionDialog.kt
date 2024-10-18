package com.lukelorusso.presentation.ui.base

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.lukelorusso.presentation.R


@Composable
fun MultiOptionDialog(
    modifier: Modifier = Modifier,
    optionLabels: List<String>,
    selectedPosition: Int,
    dismissCallback: () -> Unit = {},
    onOptionSelected: (Int) -> Unit = {}
) {
    Dialog(
        onDismissRequest = dismissCallback
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    top = 64.dp,
                    end = 16.dp,
                    bottom = 32.dp
                )
                .background(
                    color = colorResource(id = R.color.dialog_background),
                    shape = RoundedCornerShape(8.dp)
                )
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(5.dp))

            optionLabels.forEachIndexed { index, option ->
                val selected = selectedPosition == index

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1F)
                            .padding(
                                start = 16.dp,
                                end = 5.dp
                            )
                            .clickable {
                                onOptionSelected(index)
                                dismissCallback()
                            },
                        color = colorResource(id = R.color.text_color),
                        fontSize = 16.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        text = option
                    )

                    RadioButton(
                        modifier = Modifier.padding(end = 8.dp),
                        selected = selected,
                        onClick = {
                            onOptionSelected(index)
                            dismissCallback()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}
