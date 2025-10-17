package com.lukelorusso.presentation.ui.base

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lukelorusso.presentation.R


@Composable
fun YesNoAlertDialog(
    text: String,
    painter: Painter? = null,
    imageVector: ImageVector? = null,
    tint: Color? = null,
    dismissCallback: () -> Unit = {},
    confirmCallback: () -> Unit = {}
) {
    AlertDialog(
        text = {
            Row {
                painter?.let {
                    Icon(
                        modifier = Modifier
                            .size(34.dp),
                        painter = painter,
                        contentDescription = null,
                        tint = tint ?: colorResource(id = R.color.red_delete)
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                }

                imageVector?.let {
                    Icon(
                        modifier = Modifier
                            .size(34.dp),
                        imageVector = imageVector,
                        contentDescription = null,
                        tint = tint ?: colorResource(id = R.color.red_delete)
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    color = colorResource(id = R.color.text_color),
                    text = text
                )
            }
        },
        onDismissRequest = dismissCallback,
        confirmButton = {
            TextButton(onClick = confirmCallback) {
                Text(
                    text = stringResource(R.string.yes),
                    color = colorResource(id = R.color.red_delete)
                )
            }
        },
        dismissButton = {
            TextButton(onClick = dismissCallback) {
                Text(
                    text = stringResource(R.string.no),
                    color = colorResource(id = R.color.text_color),
                )
            }
        }
    )
}
