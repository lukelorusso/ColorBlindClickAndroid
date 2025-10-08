package com.lukelorusso.data.mapper

import com.lukelorusso.data.repository.impl.ColorRepositoryMockImpl
import com.lukelorusso.domain.repository.ColorRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.core.component.KoinApiExtension

class ColorMapperTest {
    private val colorMapper = ColorMapper()
    private lateinit var colorRepository: ColorRepository

    @KoinApiExtension
    @Before
    fun setup() {
        colorRepository = ColorRepositoryMockImpl(colorMapper)
    }

    @Test
    fun transform(): Unit = runBlocking {
        val color = colorRepository.decodeColorHex("#52851E", "junit")
        assertEquals(color.colorHex, "#6B8E23")
        assertEquals(color.colorName, "Olive Green")
        assertEquals(color.originalColorHex(), "#52851E")
    }
}
