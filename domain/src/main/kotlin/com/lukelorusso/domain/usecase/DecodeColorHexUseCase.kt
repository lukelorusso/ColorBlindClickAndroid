package com.lukelorusso.domain.usecase

import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.repository.ColorRepository
import com.lukelorusso.domain.usecase.base.UseCase

class DecodeColorHexUseCase(private val repository: ColorRepository) :
    UseCase<DecodeColorHexUseCase.Param, Color>() {

    override suspend fun run(param: Param): Color =
        repository.decodeColorHex(param.hex, param.deviceUdid)

    data class Param(
        val hex: String,
        val deviceUdid: String
    )

}
