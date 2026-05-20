package com.lukelorusso.colorblindclick.domain.usecase

import com.lukelorusso.colorblindclick.domain.repository.SettingsRepository
import com.lukelorusso.colorblindclick.domain.usecase.base.UseCase

class GetSaveCameraOptionsUseCase(
    private val repository: SettingsRepository
) : UseCase<Unit, Boolean>() {

    override suspend fun run(param: Unit): Boolean {
        return repository.getSaveCameraOptions()
    }
}
