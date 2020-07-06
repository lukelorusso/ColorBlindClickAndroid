package com.lukelorusso.domain.usecases

import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.repository.ColorRepository
import com.lukelorusso.domain.usecases.base.Logger
import com.lukelorusso.domain.usecases.base.SingleUseCase
import com.lukelorusso.domain.usecases.base.UseCaseScheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetColorList
@Inject constructor(
    private val repository: ColorRepository,
    useCaseScheduler: UseCaseScheduler? = null, logger: Logger? = null
) :
    SingleUseCase<List<Color>, Unit>(useCaseScheduler, logger) {

    override fun build(param: Unit): Single<List<Color>> =
        repository.getColorList()

}
