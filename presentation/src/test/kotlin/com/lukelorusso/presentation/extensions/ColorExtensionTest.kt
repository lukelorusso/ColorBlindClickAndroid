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
}
