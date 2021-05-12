@file:Suppress("DEPRECATION")

package com.lukelorusso.presentation.extensions

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.content.ContextCompat
import com.lukelorusso.presentation.R
import java.util.*

fun String.toHtml(context: Context, withChangeSize: Boolean = false): Spanned =
    let {
        // Change color
        val color =
            String.format("%X", ContextCompat.getColor(context, R.color.color_accent)).substring(2)
        it.replace("<color>", String.format("<font color=\"#%s\">", color))
            .replace("</color>", "</font>")
    }.let {
        // Change size
        if (withChangeSize) {
            it.replace("<font", "<h3><font")
                .replace("</font>", "</font></h3>")
        } else it
    }.let {
        // To Html
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(it)
        }
    }

fun String.toHtml(): Spanned =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }

fun String.capitalizeFirstLetter(): String =
    if (length > 1) substring(0, 1).toUpperCase(Locale.getDefault()) + substring(1) else this

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
