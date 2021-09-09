package com.lukelorusso.domain.usecase

import com.lukelorusso.domain.repository.ColorRepository
import com.lukelorusso.domain.usecase.base.CompletableUseCase
import io.reactivex.rxjava3.core.Completable

class SetSaveCameraOptionsUseCase(private val repository: ColorRepository) :
    CompletableUseCase<Boolean>() {

    override fun build(param: Boolean): Completable = repository.setSaveCameraOptions(param)

}
