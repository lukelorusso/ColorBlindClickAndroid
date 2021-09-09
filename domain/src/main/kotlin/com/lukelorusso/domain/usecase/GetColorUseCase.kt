package com.lukelorusso.domain.usecase

import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.repository.ColorRepository
import com.lukelorusso.domain.usecase.base.SingleUseCase
import io.reactivex.rxjava3.core.Single

class GetColorUseCase(private val repository: ColorRepository) :
    SingleUseCase<Color, GetColorUseCase.Param>() {

    override fun build(param: Param): Single<Color> =
        repository.getColor(param.hex, param.deviceUdid)

    data class Param(
        val hex: String,
        val deviceUdid: String
    )

}
