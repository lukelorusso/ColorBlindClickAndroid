package com.lukelorusso.colorblindclick.domain.usecase

import com.lukelorusso.colorblindclick.domain.repository.HistoryRepository
import com.lukelorusso.colorblindclick.domain.usecase.base.UseCase
import com.lukelorusso.domain.model.Color

class DeleteSavedColorUseCase(
    private val repository: HistoryRepository
) : UseCase<Color, Unit>() {

    override suspend fun run(param: Color) {
        repository.deleteColor(param)
    }

}
