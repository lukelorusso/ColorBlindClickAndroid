package com.lukelorusso.domain.usecase

import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.repository.ColorRepository
import com.lukelorusso.domain.usecase.base.CompletableUseCase
import io.reactivex.rxjava3.core.Completable

class DeleteColorUseCase(private val repository: ColorRepository) : CompletableUseCase<Color>() {

    override fun build(param: Color): Completable = repository.deleteColor(param)

}
