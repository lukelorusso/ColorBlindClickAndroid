package com.lukelorusso.domain.usecase

import com.lukelorusso.domain.repository.SettingsRepository
import com.lukelorusso.domain.usecase.base.UseCase

class SetPixelNeighbourhoodUseCase(
    private val repository: SettingsRepository
) : UseCase<Int, Unit>() {

    override suspend fun run(param: Int) {
        repository.setPixelNeighbourhood(param)
    }
}
