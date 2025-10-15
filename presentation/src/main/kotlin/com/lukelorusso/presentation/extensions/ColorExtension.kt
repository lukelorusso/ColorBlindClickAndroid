package com.lukelorusso.presentation.extensions

import androidx.core.graphics.ColorUtils
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * HASH: #5D362F -> RGB: 93, 54, 47
 */
fun String.hashColorToRGBDecimal(): Triple<Int, Int, Int> {
    val hash = this
        .removePrefix("#")
        .run { if (length == 8) takeLast(6) else this }
        .run { if (length == 3) "0${get(0)}0${get(1)}0${get(1)}" else this }

    if (hash.length != 6) return Triple(0, 0, 0)

    @Suppress("ReplaceSubstringWithTake")
    val intR = hash.substring(0, 2).toInt(16)
    val intG = hash.substring(2, 4).toInt(16)
    val intB = hash.substring(4, 6).toInt(16)

    return Triple(intR, intG, intB)
}

/**
 * RGB: 93, 54, 47 -> %: 36.47, 21.18, 18.43
 */
fun Triple<Int, Int, Int>.toRGBPercent(): Triple<BigDecimal, BigDecimal, BigDecimal> {
    val places = 2
    val red = BigDecimal(this.first.toDouble() / 255 * 100)
        .setScale(places, RoundingMode.HALF_UP)
    val green = BigDecimal(this.second.toDouble() / 255 * 100)
        .setScale(places, RoundingMode.HALF_UP)
    val blue = BigDecimal(this.third.toDouble() / 255 * 100)
        .setScale(places, RoundingMode.HALF_UP)
    return Triple(red, green, blue)
}

/**
 * RGB: 93, 54, 47 -> LAB: 27.215726266270266, 16.571885409527226, 11.822138971289665
 */
fun Triple<Int, Int, Int>.toLabDouble(): Triple<Double, Double, Double> {
    val labValues = DoubleArray(3)
    ColorUtils.RGBToLAB(first, second, third, labValues)
    return labValues.run {
        Triple(get(0), get(1), get(2))
    }
}
