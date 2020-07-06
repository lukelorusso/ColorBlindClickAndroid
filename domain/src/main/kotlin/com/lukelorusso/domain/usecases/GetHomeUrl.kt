package com.lukelorusso.domain.usecases

import com.lukelorusso.domain.repository.ColorRepository
import com.lukelorusso.domain.usecases.base.Logger
import com.lukelorusso.domain.usecases.base.SingleUseCase
import com.lukelorusso.domain.usecases.base.UseCaseScheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetHomeUrl
@Inject constructor(
    private val repository: ColorRepository,
    useCaseScheduler: UseCaseScheduler? = null,
    logger: Logger? = null
) :
    SingleUseCase<String, Unit>(useCaseScheduler, logger) {

    override fun build(param: Unit): Single<String> =
        repository.getHomeUrl()

}
