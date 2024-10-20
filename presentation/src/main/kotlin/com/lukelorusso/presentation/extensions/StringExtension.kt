@file:Suppress("DEPRECATION")

package com.lukelorusso.presentation.extensions

import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.compose.ui.graphics.Color
import java.text.Normalizer

fun String.toHtml(): Spanned =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }

fun String.matchSearch(searchText: String): Boolean {
    val searchWords = searchText.split(" ")

    return searchWords.all { word ->
        this.unaccented()
            .lowercase()
            .contains(word.unaccented().lowercase())
    }
}

fun String.unaccented() = Normalizer.normalize(this, Normalizer.Form.NFD)
    .replace("\u00df", "ss") // ß
    .replace("\\p{Mn}+".toRegex(), "") // https://stackoverflow.com/a/63523402

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
