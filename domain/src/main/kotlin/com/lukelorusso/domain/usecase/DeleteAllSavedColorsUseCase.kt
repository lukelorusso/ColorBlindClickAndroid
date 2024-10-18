package com.lukelorusso.domain.usecase

import com.lukelorusso.domain.repository.HistoryRepository
import com.lukelorusso.domain.usecase.base.UseCase

class DeleteAllSavedColorsUseCase(
    private val repository: HistoryRepository
) : UseCase<Unit, Unit>() {

    override suspend fun run(param: Unit) {
        repository.deleteAllColors()
    }

}
