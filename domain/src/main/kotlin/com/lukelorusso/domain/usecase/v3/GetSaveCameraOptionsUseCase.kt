package com.lukelorusso.domain.usecase.v3

import com.lukelorusso.domain.repository.v3.SettingsRepository
import com.lukelorusso.domain.usecase.v3.base.UseCase

class GetSaveCameraOptionsUseCase(
    private val repository: SettingsRepository
) : UseCase<Unit, Boolean>() {

    override suspend fun run(param: Unit): Boolean {
        return repository.getSaveCameraOptions()
    }
}
