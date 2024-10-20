package com.lukelorusso.presentation.extensions

/**
 * Zero is used to represent false, and One is used to represent true
 */
fun Boolean.toInt(): Int =
    if (this) 1 else 0
