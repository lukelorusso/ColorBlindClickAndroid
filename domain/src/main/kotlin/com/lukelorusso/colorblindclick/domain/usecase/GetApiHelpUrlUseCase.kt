package com.lukelorusso.colorblindclick.domain.usecase

import com.lukelorusso.colorblindclick.domain.repository.ApiRepository.Companion.getApiByLanguage
import com.lukelorusso.colorblindclick.domain.repository.SaveDevApiRepository
import com.lukelorusso.colorblindclick.domain.repository.SettingsRepository
import com.lukelorusso.colorblindclick.domain.repository.TheColorApiRepository
import com.lukelorusso.colorblindclick.domain.usecase.base.UseCase

class GetApiHelpUrlUseCase(
    private val theColorApiRepository: TheColorApiRepository,
    private val saveDevApiRepository: SaveDevApiRepository,
    private val settingsRepository: SettingsRepository
) : UseCase<Unit, String>() {

    override suspend fun run(param: Unit): String {
        val deviceLanguage = settingsRepository.getDeviceLanguage()

        return getApiByLanguage(
            deviceLanguage,
            theColorApiRepository,
            saveDevApiRepository
        ).getHelpUrl(deviceLanguage)
    }
}
