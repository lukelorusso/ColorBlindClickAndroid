package com.lukelorusso.domain.usecase

import com.lukelorusso.domain.repository.ColorRepository
import com.lukelorusso.domain.usecase.base.SingleUseCase
import io.reactivex.rxjava3.core.Single

class GetSaveCameraOptionsUseCase(private val repository: ColorRepository) :
    SingleUseCase<Boolean, Unit>() {

    override fun build(param: Unit): Single<Boolean> = repository.getSaveCameraOptions()

}
