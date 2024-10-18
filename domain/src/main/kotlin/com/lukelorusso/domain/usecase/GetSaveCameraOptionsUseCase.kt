package com.lukelorusso.domain.usecase

import com.lukelorusso.domain.repository.SettingsRepository
import com.lukelorusso.domain.usecase.base.UseCase

class GetSaveCameraOptionsUseCase(
    private val repository: SettingsRepository
) : UseCase<Unit, Boolean>() {

    override suspend fun run(param: Unit): Boolean {
        return repository.getSaveCameraOptions()
    }
}
