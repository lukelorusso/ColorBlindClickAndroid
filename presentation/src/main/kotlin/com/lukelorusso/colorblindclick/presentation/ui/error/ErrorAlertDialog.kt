package com.lukelorusso.colorblindclick.presentation.ui.error

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lukelorusso.colorblindclick.presentation.R
import com.lukelorusso.colorblindclick.presentation.ui.icons.Warning


@Composable
fun ErrorAlertDialog(
    message: String? = null,
    dismissCallback: () -> Unit = {},
    confirmCallback: () -> Unit = {}
) {
    AlertDialog(
        containerColor = colorResource(id = R.color.red_delete),
        text = {
            Row {
                Icon(
                    modifier = Modifier
                        .size(32.dp),
                    imageVector = Warning,
                    contentDescription = null,
                    tint = Color.White
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = message ?: stringResource(R.string.action_label_undefined_error),
                    color = Color.White
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
