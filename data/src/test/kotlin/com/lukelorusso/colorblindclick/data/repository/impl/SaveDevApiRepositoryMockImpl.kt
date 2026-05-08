package com.lukelorusso.colorblindclick.data.repository.impl

import com.lukelorusso.colorblindclick.data.mapper.SaveDevMapper
import com.lukelorusso.colorblindclick.data.net.dto.SaveDevResponseDTO
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.repository.SaveDevApiRepository
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.InputStream

class SaveDevApiRepositoryMockImpl(
    private val colorMapper: SaveDevMapper
) : SaveDevApiRepository {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun decodeColorHex(colorHex: String, deviceLanguage: String, deviceUdid: String): Color {
        val colorResponseDTO = parseDtoFromJson()
        return colorMapper.transform(colorResponseDTO)
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun parseDtoFromJson(): SaveDevResponseDTO {
        val inputStream: InputStream =
            this::class.java.classLoader?.getResourceAsStream(COLOR_FILENAME) ?: error("Failed to load $COLOR_FILENAME")
        return json.decodeFromStream<SaveDevResponseDTO>(inputStream)
    }

    companion object {
        private const val COLOR_FILENAME = "savedev_api_response.json"
    }

    override fun getHomeUrl(deviceLanguage: String): String =
        "https://info.cern.ch/hypertext/WWW/TheProject.html"

    override fun getHelpUrl(deviceLanguage: String): String =
        "https://info.cern.ch/hypertext/WWW/TheProject.html"
}
