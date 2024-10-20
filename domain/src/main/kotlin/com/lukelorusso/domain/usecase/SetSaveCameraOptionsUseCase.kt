package com.lukelorusso.domain.usecase

import com.lukelorusso.domain.repository.SettingsRepository
import com.lukelorusso.domain.usecase.base.UseCase

class SetSaveCameraOptionsUseCase(
    private val repository: SettingsRepository
) : UseCase<Boolean, Unit>() {

    override suspend fun run(param: Boolean) {
        repository.setSaveCameraOptions(param)
    }
}
