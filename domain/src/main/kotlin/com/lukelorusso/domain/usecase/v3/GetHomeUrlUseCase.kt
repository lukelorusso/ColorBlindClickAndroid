package com.lukelorusso.domain.usecase.v3

import com.lukelorusso.domain.repository.v3.InfoRepository
import com.lukelorusso.domain.usecase.v3.base.UseCase


class GetHomeUrlUseCase(private val repository: InfoRepository) :
    UseCase<Unit, String>() {

    override suspend fun run(param: Unit): String {
        return repository.getHomeUrl()
    }
}
