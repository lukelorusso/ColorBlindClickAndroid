package com.lukelorusso.colorblindclick.presentation.ui.base

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lukelorusso.colorblindclick.presentation.R

/**
 * A text-input dialog used to set/edit a free-form tag on a saved color.
 */
@Composable
fun TagAlertDialog(
    initialTag: String?,
    dismissCallback: () -> Unit = {},
    confirmCallback: (String?) -> Unit
) {
    var tagText by remember { mutableStateOf(initialTag.orEmpty()) }

    AlertDialog(
        title = {
            Text(
                color = colorResource(id = R.color.text_color),
                text = stringResource(R.string.color_tag_dialog_title)
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    modifier = Modifier.padding(top = 4.dp),
                    value = tagText,
                    onValueChange = { tagText = it },
                    placeholder = { Text(stringResource(R.string.color_tag_dialog_hint)) },
                    singleLine = true
                )
            }
        },
        onDismissRequest = dismissCallback,
        confirmButton = {
            TextButton(onClick = { confirmCallback(tagText.trim().ifBlank { null }) }) {
                Text(
                    text = stringResource(R.string.action_label_ok),
                    color = colorResource(id = R.color.text_color)
                )
            }
        },
        dismissButton = {
            TextButton(onClick = dismissCallback) {
                Text(
                    text = stringResource(R.string.action_label_dismiss),
                    color = colorResource(id = R.color.text_color)
                )
            }
        }
    )
}
