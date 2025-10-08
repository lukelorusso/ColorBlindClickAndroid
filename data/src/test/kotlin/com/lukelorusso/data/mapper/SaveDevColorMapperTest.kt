package com.lukelorusso.data.mapper

import com.lukelorusso.data.repository.impl.SaveDevRepositoryMockImpl
import com.lukelorusso.domain.repository.SaveDevRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.core.component.KoinApiExtension

class SaveDevColorMapperTest {
    private val colorMapper = SaveDevMapper()
    private lateinit var repository: SaveDevRepository

    @KoinApiExtension
    @Before
    fun setup() {
        repository = SaveDevRepositoryMockImpl(colorMapper)
    }

    @Test
    fun transform(): Unit = runBlocking {
        val color = repository.decodeColorHex("#52851E", "junit")
        assertEquals(color.colorHex, "#6B8E23")
        assertEquals(color.colorName, "Olive Green")
        assertEquals(color.originalColorHex(), "#52851E")
    }
}
