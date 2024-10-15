package com.lukelorusso.presentation.ui.v3.history

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.dp
import com.lukelorusso.presentation.R


@Composable
fun DeleteAllAlertDialog(
    dismissCallback: () -> Unit = {},
    confirmCallback: () -> Unit = {}
) {
    AlertDialog(
        text = {
            Row {
                Icon(
                    modifier = Modifier
                        .size(34.dp),
                    painter = painterResource(id = R.drawable.delete_sweep_white),
                    tint = colorResource(id = R.color.red_delete),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    color = colorResource(id = R.color.text_color),
                    text = stringResource(R.string.color_delete_all_confirmation_message)
                )
            }
        },
        onDismissRequest = dismissCallback,
        confirmButton = {
            TextButton(
                onClick = {
                    confirmCallback()
                    dismissCallback()
                }
            ) {
                Text(
                    text = stringResource(R.string.yes),
                    color = colorResource(id = R.color.red_delete)
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    dismissCallback()
                }
            ) {
                Text(
                    text = stringResource(R.string.no),
                    color = colorResource(id = R.color.text_color),
                )
            }
        }
    )
}
