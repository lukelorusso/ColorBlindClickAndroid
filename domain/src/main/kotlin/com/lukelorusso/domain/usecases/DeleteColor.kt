package com.lukelorusso.domain.usecases

import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.repository.ColorRepository
import com.lukelorusso.domain.usecases.base.CompletableUseCase
import com.lukelorusso.domain.usecases.base.Logger
import com.lukelorusso.domain.usecases.base.UseCaseScheduler
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class DeleteColor
@Inject constructor(
    private val repository: ColorRepository,
    useCaseScheduler: UseCaseScheduler? = null, logger: Logger? = null
) :
    CompletableUseCase<Color>(useCaseScheduler, logger) {

    override fun build(param: Color): Completable = repository.deleteColor(param)

}
