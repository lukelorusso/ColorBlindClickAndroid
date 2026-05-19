package com.lukelorusso.colorblindclick.domain.usecase

import com.lukelorusso.colorblindclick.domain.entity.ColorEntity
import com.lukelorusso.colorblindclick.domain.repository.HistoryRepository
import com.lukelorusso.colorblindclick.domain.usecase.base.UseCase

class GetSavedColorListUseCase(
    private val repository: HistoryRepository
) : UseCase<Unit, List<ColorEntity>>() {
    override val logResult: Boolean = false // the result could be long

    override suspend fun run(param: Unit): List<ColorEntity> {
        return repository.getColorList()
    }

}
