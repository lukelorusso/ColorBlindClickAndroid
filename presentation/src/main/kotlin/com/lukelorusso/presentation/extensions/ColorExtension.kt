package com.lukelorusso.presentation.extensions

import com.lukelorusso.domain.model.Color
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

const val LINE_BREAK = "\n"

/**
 * PIXEL: -10668497 -> RGB: 93, 54, 47 -> HASH: #5D362F
 */
fun Int.pixelColorToHash(): String {
    val hashR = Integer.toHexString(android.graphics.Color.red(this)).let {
        if (it.length == 1) "0$it" else it
    }
    val hashG = Integer.toHexString(android.graphics.Color.green(this)).let {
        if (it.length == 1) "0$it" else it
    }
    val hashB = Integer.toHexString(android.graphics.Color.blue(this)).let {
        if (it.length == 1) "0$it" else it
    }
    return "#$hashR$hashG$hashB"
}

/**
 * HASH: #5D362F -> PIXEL: -10668497
 */
fun String.hashColorToPixel(): Int =
        android.graphics.Color.parseColor(this)

/**
 * HASH: #5D362F -> [36.470588235294116, 21.176470588235293, 18.431372549019606]
 */
fun String.hashColorToPercent(): List<Double> {
    val pixelColor = this.hashColorToPixel()
    val percentR = android.graphics.Color.red(pixelColor).toDouble() / 255 * 100
    val percentG = android.graphics.Color.green(pixelColor).toDouble() / 255 * 100
    val percentB = android.graphics.Color.blue(pixelColor).toDouble() / 255 * 100
    return listOf(percentR, percentG, percentB)
}


fun Color.sharableDescription(credits: String): String {
    return (this.colorName + LINE_BREAK
            + this.colorHex.toUpperCase(Locale.getDefault()) + LINE_BREAK
            + this.toRGBString() + LINE_BREAK
            + LINE_BREAK
            + credits)
}

fun Color.toRGBString(): String {
    val colors = this.colorHex.hashColorToPercent()
    val places = 2
    var red = BigDecimal(colors[0])
    red = red.setScale(places, RoundingMode.HALF_UP)
    var green = BigDecimal(colors[1])
    green = green.setScale(places, RoundingMode.HALF_UP)
    var blue = BigDecimal(colors[2])
    blue = blue.setScale(places, RoundingMode.HALF_UP)
    return "R: $red, G: $green, B: $blue"
}
