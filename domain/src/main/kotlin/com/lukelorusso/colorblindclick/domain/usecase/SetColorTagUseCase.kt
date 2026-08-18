package com.lukelorusso.colorblindclick.domain.usecase

import com.lukelorusso.colorblindclick.domain.entity.ColorEntity
import com.lukelorusso.colorblindclick.domain.repository.HistoryRepository
import com.lukelorusso.colorblindclick.domain.usecase.base.UseCase

class SetColorTagUseCase(
    private val repository: HistoryRepository
) : UseCase<SetColorTagUseCase.Params, ColorEntity>() {
    data class Params(val color: ColorEntity, val tag: String?)

    override suspend fun run(param: Params): ColorEntity =
        repository.updateColorTag(param.color, param.tag)

}
