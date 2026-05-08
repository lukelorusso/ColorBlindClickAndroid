package com.lukelorusso.colorblindclick.domain.usecase

import com.lukelorusso.colorblindclick.domain.repository.HistoryRepository
import com.lukelorusso.colorblindclick.domain.usecase.base.UseCase

class DeleteAllSavedColorsUseCase(
    private val repository: HistoryRepository
) : UseCase<Unit, Unit>() {

    override suspend fun run(param: Unit) {
        repository.deleteAllColors()
    }

}
