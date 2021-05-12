package com.lukelorusso.domain.usecase

import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.repository.ColorRepository
import com.lukelorusso.domain.usecase.base.SingleUseCase
import io.reactivex.rxjava3.core.Single

class GetColorListUseCase(private val repository: ColorRepository) :
    SingleUseCase<List<Color>, Unit>() {

    override fun build(param: Unit): Single<List<Color>> =
        repository.getColorList()

}
