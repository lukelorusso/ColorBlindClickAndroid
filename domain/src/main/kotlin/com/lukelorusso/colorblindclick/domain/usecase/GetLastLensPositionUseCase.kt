package com.lukelorusso.colorblindclick.domain.usecase

import com.lukelorusso.colorblindclick.domain.repository.SettingsRepository
import com.lukelorusso.colorblindclick.domain.usecase.base.UseCase

class GetLastLensPositionUseCase(
    private val repository: SettingsRepository
) : UseCase<Unit, Int>() {

    override suspend fun run(param: Unit): Int {
        return repository.getLastLensPosition()
    }

}
