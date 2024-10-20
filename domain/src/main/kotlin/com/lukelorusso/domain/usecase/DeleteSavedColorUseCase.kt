package com.lukelorusso.domain.usecase

import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.repository.HistoryRepository
import com.lukelorusso.domain.usecase.base.UseCase

class DeleteSavedColorUseCase(
    private val repository: HistoryRepository
) : UseCase<Color, Unit>() {

    override suspend fun run(param: Color) {
        repository.deleteColor(param)
    }

}
