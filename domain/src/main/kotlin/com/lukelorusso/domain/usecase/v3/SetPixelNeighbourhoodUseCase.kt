package com.lukelorusso.domain.usecase.v3

import com.lukelorusso.domain.repository.v3.SettingsRepository
import com.lukelorusso.domain.usecase.v3.base.UseCase

class SetPixelNeighbourhoodUseCase(
    private val repository: SettingsRepository
) : UseCase<Int, Unit>() {

    override suspend fun run(param: Int) {
        repository.setPixelNeighbourhood(param)
    }
}
