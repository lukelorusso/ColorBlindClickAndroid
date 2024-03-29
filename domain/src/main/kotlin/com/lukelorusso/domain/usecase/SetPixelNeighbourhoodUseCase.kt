package com.lukelorusso.domain.usecase

import com.lukelorusso.domain.repository.ColorRepository
import com.lukelorusso.domain.usecase.base.CompletableUseCase
import io.reactivex.rxjava3.core.Completable

class SetPixelNeighbourhoodUseCase(private val repository: ColorRepository) :
    CompletableUseCase<Int>() {

    override fun build(param: Int): Completable = repository.setPixelNeighbourhood(param)

}
