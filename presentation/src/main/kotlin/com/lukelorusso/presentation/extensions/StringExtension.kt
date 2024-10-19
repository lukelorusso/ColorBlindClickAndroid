@file:Suppress("DEPRECATION")

package com.lukelorusso.presentation.extensions

import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.compose.ui.graphics.Color
import java.util.*

fun String.toHtml(): Spanned =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }

fun String.containsWords(words: String?): Boolean {
    if (words == null) {
        return false
    }

    val wordList = listOf(
        *words.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    )

    var matchCounter = 0
    for (word in wordList) {
        if (this.toLowerCase(Locale.getDefault()).contains(word.toLowerCase(Locale.getDefault()))) {
            matchCounter++
        }
    }

    return wordList.size == matchCounter
}

fun String.parseToColor(): Color {
    val parsableColor = when (this.length) {
        7 ->
            this.replaceFirst("#", "FF")

        9 ->
            this.replaceFirst("#", "")

        else ->
            "FF000000"
    }

    return Color(parsableColor.toLong(radix = 16))
}
