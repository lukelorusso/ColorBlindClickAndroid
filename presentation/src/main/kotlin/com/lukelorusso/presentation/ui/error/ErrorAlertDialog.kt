package com.lukelorusso.presentation.ui.error

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lukelorusso.presentation.R


@Composable
fun ErrorAlertDialog(
    message: String? = null,
    dismissCallback: () -> Unit = {},
    confirmCallback: () -> Unit = {}
) {
    AlertDialog(
        backgroundColor = MaterialTheme.colors.error,
        text = {
            Row {
                Icon(
                    modifier = Modifier
                        .size(32.dp),
                    imageVector = Icons.Default.Warning,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = message ?: stringResource(R.string.action_label_undefined_error)
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
                    text = stringResource(R.string.action_label_ok),
                    color = Color.White
                )
            }
        }
    )
}
