package com.lukelorusso.domain.usecase

import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.repository.*
import com.lukelorusso.domain.repository.ApiRepository.Companion.getApiByLanguage
import com.lukelorusso.domain.usecase.base.UseCase

class DecodeColorHexUseCase(
    private val theColorApiRepository: TheColorApiRepository,
    private val saveDevApiRepository: SaveDevApiRepository,
    private val settingsRepository: SettingsRepository
) : UseCase<DecodeColorHexUseCase.Param, Color>() {

    override suspend fun run(param: Param): Color {
        val deviceLanguage = settingsRepository.getDeviceLanguage()

        return getApiByLanguage(
            deviceLanguage,
            theColorApiRepository,
            saveDevApiRepository
        ).decodeColorHex(
            param.colorHex,
            deviceLanguage,
            param.deviceUdid
        )
    }

    data class Param(
        val colorHex: String,
        val deviceUdid: String
    )
}
