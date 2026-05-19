package com.lukelorusso.colorblindclick.domain.repository

import com.lukelorusso.colorblindclick.domain.entity.ColorEntity

interface ApiRepository {

    suspend fun decodeColorHex(colorHex: String, deviceLanguage: String, deviceUdid: String): ColorEntity

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
