package com.lukelorusso.domain.usecase

import com.lukelorusso.domain.repository.ColorRepository
import com.lukelorusso.domain.usecase.base.SingleUseCase
import io.reactivex.rxjava3.core.Single

class GetLastLensPositionUseCase(private val repository: ColorRepository) :
    SingleUseCase<Int, Unit>() {

    override fun build(param: Unit): Single<Int> = repository.getLastLensPosition()

}
