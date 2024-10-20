package com.lukelorusso.data.repository.impl

import com.google.gson.Gson
import com.lukelorusso.data.mapper.ColorMapper
import com.lukelorusso.data.net.dto.ColorResponseDTO
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.repository.ColorRepository
import java.io.InputStream

class ColorRepositoryMockImpl(
    private val gson: Gson,
    private val colorMapper: ColorMapper
) : ColorRepository {

    override suspend fun decodeColorHex(colorHex: String, deviceUdid: String): Color {
        val colorResponseDTO = parseDtoFromJson()
        return colorMapper.transform(colorResponseDTO)
    }

    private fun parseDtoFromJson(): ColorResponseDTO {
        val inputStream: InputStream =
            this::class.java.classLoader?.getResourceAsStream(COLOR_FILENAME) ?: error("Failed to load $COLOR_FILENAME")
        return gson.fromJson(inputStream.bufferedReader(), ColorResponseDTO::class.java)
    }

    companion object {
        private const val COLOR_FILENAME = "color.json"
    }

}
