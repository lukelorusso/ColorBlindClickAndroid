package com.lukelorusso.domain.usecase

import com.lukelorusso.domain.repository.*
import com.lukelorusso.domain.repository.ApiRepository.Companion.getApiByLanguage
import com.lukelorusso.domain.usecase.base.UseCase

class GetApiHomeUrlUseCase(
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
        ).getHomeUrl(deviceLanguage)
    }
}
