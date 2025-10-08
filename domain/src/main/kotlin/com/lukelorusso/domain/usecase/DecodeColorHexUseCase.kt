package com.lukelorusso.domain.usecase

import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.repository.*
import com.lukelorusso.domain.usecase.base.UseCase

class DecodeColorHexUseCase(
    private val saveDevRepository: SaveDevRepository,
    private val theColorRepository: TheColorRepository,
    private val settingsRepository: SettingsRepository
) : UseCase<DecodeColorHexUseCase.Param, Color>() {

    override suspend fun run(param: Param): Color {
        val deviceLanguage = settingsRepository.getDeviceLanguage()

        return if (deviceLanguage == "en")
            theColorRepository.decodeColorHex(param.hex)
        else
            saveDevRepository.decodeColorHex(
                param.hex,
                deviceLanguage,
                param.deviceUdid
            )
    }

    data class Param(
        val hex: String,
        val deviceUdid: String
    )

}
