package com.lukelorusso.data.repository.impl

import com.lukelorusso.data.mapper.TheColorMapper
import com.lukelorusso.data.net.dto.TheColorResponseDTO
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.repository.TheColorApiRepository
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.InputStream

class TheColorApiRepositoryMockImpl(
    private val colorMapper: TheColorMapper
) : TheColorApiRepository {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun decodeColorHex(colorHex: String, deviceLanguage: String, deviceUdid: String): Color {
        val colorResponseDTO = parseDtoFromJson()
        return colorMapper.transform(colorResponseDTO)
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun parseDtoFromJson(): TheColorResponseDTO {
        val inputStream: InputStream =
            this::class.java.classLoader?.getResourceAsStream(COLOR_FILENAME) ?: error("Failed to load $COLOR_FILENAME")
        return json.decodeFromStream<TheColorResponseDTO>(inputStream)
    }

    companion object {
        private const val COLOR_FILENAME = "thecolor_api_response.json"
    }

    override fun getHomeUrl(deviceLanguage: String): String =
        "https://info.cern.ch/hypertext/WWW/TheProject.html"

    override fun getHelpUrl(deviceLanguage: String): String =
        "https://info.cern.ch/hypertext/WWW/TheProject.html"
}
