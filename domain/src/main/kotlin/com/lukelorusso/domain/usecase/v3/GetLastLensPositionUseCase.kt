package com.lukelorusso.domain.usecase.v3

import com.lukelorusso.domain.repository.v3.SettingsRepository
import com.lukelorusso.domain.usecase.v3.base.UseCase

class GetLastLensPositionUseCase(
    private val repository: SettingsRepository
) : UseCase<Unit, Int>() {

    override suspend fun run(param: Unit): Int {
        return repository.getLastLensPosition()
    }

}
