package com.lukelorusso.domain.usecase.v3

import com.lukelorusso.domain.repository.v3.SettingsRepository
import com.lukelorusso.domain.usecase.v3.base.UseCase

class SetSaveCameraOptionsUseCase(
    private val repository: SettingsRepository
) : UseCase<Boolean, Unit>() {

    override suspend fun run(param: Boolean) {
        repository.setSaveCameraOptions(param)
    }
}
