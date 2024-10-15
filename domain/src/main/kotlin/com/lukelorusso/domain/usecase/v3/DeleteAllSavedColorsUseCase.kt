package com.lukelorusso.domain.usecase.v3

import com.lukelorusso.domain.repository.v3.HistoryRepository
import com.lukelorusso.domain.usecase.v3.base.UseCase

class DeleteAllSavedColorsUseCase(
    private val repository: HistoryRepository
) : UseCase<Unit, Unit>() {

    override suspend fun run(param: Unit) {
        repository.deleteAllColors()
    }

}
