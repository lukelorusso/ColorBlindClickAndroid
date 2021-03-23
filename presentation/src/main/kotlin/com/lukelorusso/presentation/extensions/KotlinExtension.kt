package com.lukelorusso.presentation.extensions

fun Boolean.alsoTrue(block: (Boolean) -> Unit) {
    also { if (it) block(it) }
}
