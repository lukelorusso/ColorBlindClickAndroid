package com.lukelorusso.domain.usecase

import com.lukelorusso.domain.repository.InfoRepository
import com.lukelorusso.domain.usecase.base.UseCase

class GetStoreUrlUseCase(
    private val repository: InfoRepository
) : UseCase<Unit, String>() {

    override suspend fun run(param: Unit): String {
        return repository.getStoreUrl()
    }

}
