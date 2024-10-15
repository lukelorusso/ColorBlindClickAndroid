package com.lukelorusso.domain.usecase.v3

import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.repository.v3.HistoryRepository
import com.lukelorusso.domain.usecase.v3.base.UseCase

class DeleteSavedColorUseCase(
    private val repository: HistoryRepository
) : UseCase<Color, Unit>() {

    override suspend fun run(param: Color) {
        repository.deleteColor(param)
    }

}
