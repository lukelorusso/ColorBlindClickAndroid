package com.lukelorusso.colorblindclick.domain.usecase

import com.lukelorusso.colorblindclick.domain.repository.HistoryRepository
import com.lukelorusso.colorblindclick.domain.usecase.base.UseCase
import com.lukelorusso.domain.model.Color

class GetSavedColorListUseCase(
    private val repository: HistoryRepository
) : UseCase<Unit, List<Color>>() {
    override val logResult: Boolean = false // the result could be long

    override suspend fun run(param: Unit): List<Color> {
        return repository.getColorList()
    }

}
