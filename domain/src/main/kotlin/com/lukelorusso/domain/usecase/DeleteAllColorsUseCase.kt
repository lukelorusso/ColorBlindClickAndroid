package com.lukelorusso.domain.usecase

import com.lukelorusso.domain.repository.ColorRepository
import com.lukelorusso.domain.usecase.base.CompletableUseCase
import io.reactivex.rxjava3.core.Completable

class DeleteAllColorsUseCase(private val repository: ColorRepository) : CompletableUseCase<Unit>() {

    override fun build(param: Unit): Completable = repository.deleteAllColors()

}
