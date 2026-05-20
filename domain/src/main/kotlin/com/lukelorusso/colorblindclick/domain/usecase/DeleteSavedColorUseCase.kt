package com.lukelorusso.colorblindclick.domain.usecase

import com.lukelorusso.colorblindclick.domain.entity.ColorEntity
import com.lukelorusso.colorblindclick.domain.repository.HistoryRepository
import com.lukelorusso.colorblindclick.domain.usecase.base.UseCase

class DeleteSavedColorUseCase(
    private val repository: HistoryRepository
) : UseCase<ColorEntity, Unit>() {

    override suspend fun run(param: ColorEntity) {
        repository.deleteColor(param)
    }

}
