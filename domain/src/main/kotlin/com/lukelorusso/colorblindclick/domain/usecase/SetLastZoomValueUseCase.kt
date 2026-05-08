package com.lukelorusso.colorblindclick.domain.usecase

import com.lukelorusso.colorblindclick.domain.repository.SettingsRepository
import com.lukelorusso.colorblindclick.domain.usecase.base.UseCase

class SetLastZoomValueUseCase(
    private val repository: SettingsRepository
) : UseCase<Int, Unit>() {

    override suspend fun run(param: Int) {
        repository.setLastZoomValue(param)
    }
}
