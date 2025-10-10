package com.lukelorusso.data.mapper

import com.lukelorusso.data.repository.impl.TheColorApiRepositoryMockImpl
import com.lukelorusso.domain.repository.TheColorApiRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TheColorMapperTest {
    private val colorMapper = TheColorMapper()
    private lateinit var repository: TheColorApiRepository

    @Before
    fun setup() {
        repository = TheColorApiRepositoryMockImpl(colorMapper)
    }

    @Test
    fun transform(): Unit = runBlocking {
        val color = repository.decodeColorHex("#2AB564", "en", "junit")
        assertEquals(color.colorHex, "#29AB87")
        assertEquals(color.colorName, "Jungle Green")
        assertEquals(color.originalColorHex(), "#2AB564")
    }
}
