package com.lukelorusso.domain.usecase

import com.lukelorusso.domain.repository.InfoRepository
import com.lukelorusso.domain.repository.SettingsRepository
import com.lukelorusso.domain.usecase.base.UseCase

class GetApiHelpUrlUseCase(
    private val infoRepository: InfoRepository,
    private val settingsRepository: SettingsRepository
) : UseCase<Unit, String>() {

    override suspend fun run(param: Unit): String {
        return infoRepository.getApiHelpUrl(settingsRepository.getDeviceLanguage())
    }

}
