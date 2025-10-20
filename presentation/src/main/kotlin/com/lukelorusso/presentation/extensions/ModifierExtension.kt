package com.lukelorusso.presentation.extensions

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier

fun Modifier.clickableWithoutRipple(
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
) = clickable(
    interactionSource = interactionSource,
    indication = indication,
    enabled = enabled,
    onClick = onClick
)
