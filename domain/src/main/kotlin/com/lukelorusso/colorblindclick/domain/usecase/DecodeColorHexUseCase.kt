package com.lukelorusso.colorblindclick.domain.usecase

import com.lukelorusso.colorblindclick.domain.entity.ColorEntity
import com.lukelorusso.colorblindclick.domain.repository.ApiRepository.Companion.getApiByLanguage
import com.lukelorusso.colorblindclick.domain.repository.SaveDevApiRepository
import com.lukelorusso.colorblindclick.domain.repository.SettingsRepository
import com.lukelorusso.colorblindclick.domain.repository.TheColorApiRepository
import com.lukelorusso.colorblindclick.domain.usecase.base.UseCase

class DecodeColorHexUseCase(
    private val theColorApiRepository: TheColorApiRepository,
    private val saveDevApiRepository: SaveDevApiRepository,
    private val settingsRepository: SettingsRepository
) : UseCase<DecodeColorHexUseCase.Param, ColorEntity>() {

    override suspend fun run(param: Param): ColorEntity {
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
