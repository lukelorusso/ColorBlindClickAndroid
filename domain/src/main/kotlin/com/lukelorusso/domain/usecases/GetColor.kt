package com.lukelorusso.domain.usecases

import com.lukelorusso.domain.functions.ConnectionFilter
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.repository.ColorRepository
import com.lukelorusso.domain.usecases.base.Logger
import com.lukelorusso.domain.usecases.base.SingleUseCase
import com.lukelorusso.domain.usecases.base.UseCaseScheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetColor
@Inject constructor(
        private val repository: ColorRepository,
        useCaseScheduler: UseCaseScheduler? = null, logger: Logger? = null
) :
        SingleUseCase<Color, GetColor.Param>(useCaseScheduler, logger) {

    override fun build(param: Param): Single<Color> =
            Single.just(repository.isConnected)
                    .filter(ConnectionFilter())
                    .flatMapSingle { repository.getColor(param.colorHash, param.deviceUdid) }
                    .toSingle()

    data class Param(
            val colorHash: String,
            val deviceUdid: String
    )

}
