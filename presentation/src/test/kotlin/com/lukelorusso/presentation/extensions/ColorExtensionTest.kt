package com.lukelorusso.presentation.extensions

import org.junit.Test

class ColorExtensionTest {

    @Test
    fun hashColorToRGBDecimalTest() {
        val hashColor = "#5D362F"
        val percentTriple = hashColor.hashColorToRGBDecimal()
        assert(percentTriple == Triple(93, 54, 47))
    }

    @Test
    fun hashColorToRGBDecimalTest_wrongHash() {
        val hashColor = "#3"
        val percentTriple = hashColor.hashColorToRGBDecimal()
        assert(percentTriple == Triple(0, 0, 0))
    }

    @Test
    fun toRGBPercentStringTest() {
        val rgbTriple = Triple(93, 54, 47)
        val percentString = rgbTriple.toRGBPercentString()
        assert(percentString == "rgb(36.47%, 21.18%, 18.43%)")
    }

    @Test
    fun toLabDoubleTest() {
        val rgbTriple = Triple(93, 54, 47)
        val labDouble = rgbTriple.toLabDouble()
        assert(labDouble == Triple(27.215726266270266, 16.571885409527226, 11.822138971289665))
    }

    @Test
    fun closestHtmlColorTest() {
        val rgbTriple = Triple(93, 54, 47)
        val closestBasicColor = rgbTriple.closestHtmlColor()
        assert(closestBasicColor == HtmlColor.Maroon)
    }
}
