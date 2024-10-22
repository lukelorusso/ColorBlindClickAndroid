package com.lukelorusso.data.repository.impl

import com.lukelorusso.data.mapper.ColorMapper
import com.lukelorusso.data.net.dto.ColorResponseDTO
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.repository.ColorRepository
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.InputStream

class ColorRepositoryMockImpl(
    private val colorMapper: ColorMapper
) : ColorRepository {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun decodeColorHex(colorHex: String, deviceUdid: String): Color {
        val colorResponseDTO = parseDtoFromJson()
        return colorMapper.transform(colorResponseDTO)
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun parseDtoFromJson(): ColorResponseDTO {
        val inputStream: InputStream =
            this::class.java.classLoader?.getResourceAsStream(COLOR_FILENAME) ?: error("Failed to load $COLOR_FILENAME")
        return json.decodeFromStream<ColorResponseDTO>(inputStream)
    }

    companion object {
        private const val COLOR_FILENAME = "color.json"
    }

}
