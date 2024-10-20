package com.lukelorusso.domain.usecase

import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.repository.HistoryRepository
import com.lukelorusso.domain.usecase.base.UseCase

class GetSavedColorListUseCase(
    private val repository: HistoryRepository
) : UseCase<Unit, List<Color>>() {

    override suspend fun run(param: Unit): List<Color> {
        return repository.getColorList()
    }

}
