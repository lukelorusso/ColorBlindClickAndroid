package com.lukelorusso.presentation.extensions

import org.junit.Test
import java.math.BigDecimal
import java.math.RoundingMode

class ColorExtensionTest {

    @Test
    fun hashColorToRGBDecimalTest() {
        val hashColor = "#5D362F"
        val decimalTriple = hashColor.hashColorToRGBDecimal()
        assert(decimalTriple == Triple(93, 54, 47))
    }

    @Test
    fun hashColorToRGBDecimalTest_wrongHash() {
        val hashColor = "#3"
        val decimalTriple = hashColor.hashColorToRGBDecimal()
        assert(decimalTriple == Triple(0, 0, 0))
    }

    @Test
    fun toRGBPercentTest() {
        val decimalTriple = Triple(93, 54, 47)
        val percentTriple = decimalTriple.toRGBPercent()
        assert(percentTriple == Triple(
            BigDecimal(36.47).setScale(2, RoundingMode.HALF_UP),
            BigDecimal(21.18).setScale(2, RoundingMode.HALF_UP),
            BigDecimal(18.43).setScale(2, RoundingMode.HALF_UP)
        ))
    }

    @Test
    fun toLabDoubleTest() {
        val decimalTriple = Triple(93, 54, 47)
        val labTriple = decimalTriple.toLabDouble()
        assert(labTriple == Triple(27.215726266270266, 16.571885409527226, 11.822138971289665))
    }

    @Test
    fun closestHtmlColorTest() {
        val decimalTriple = Triple(93, 54, 47)
        val closestBasicColor = decimalTriple.closestHtmlColor()
        assert(closestBasicColor == HtmlColor.Maroon)
    }
}
