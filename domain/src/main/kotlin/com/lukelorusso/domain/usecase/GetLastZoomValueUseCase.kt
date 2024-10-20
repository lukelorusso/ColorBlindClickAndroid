package com.lukelorusso.domain.usecase

import com.lukelorusso.domain.repository.SettingsRepository
import com.lukelorusso.domain.usecase.base.UseCase

class GetLastZoomValueUseCase(
    private val repository: SettingsRepository
) : UseCase<Unit, Int>() {

    override suspend fun run(param: Unit): Int {
        return repository.getLastZoomValue()
    }

}
