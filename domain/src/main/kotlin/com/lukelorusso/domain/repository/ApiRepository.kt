package com.lukelorusso.domain.repository

import com.lukelorusso.domain.model.Color

interface ApiRepository {

    suspend fun decodeColorHex(colorHex: String, deviceLanguage: String, deviceUdid: String): Color

    fun getHomeUrl(deviceLanguage: String): String

    fun getHelpUrl(deviceLanguage: String): String

    companion object {
        fun getApiByLanguage(
            deviceLanguage: String,
            theColorApiRepository: TheColorApiRepository,
            saveDevApiRepository: SaveDevApiRepository
        ) = if (deviceLanguage == "en") theColorApiRepository else saveDevApiRepository
    }
}
