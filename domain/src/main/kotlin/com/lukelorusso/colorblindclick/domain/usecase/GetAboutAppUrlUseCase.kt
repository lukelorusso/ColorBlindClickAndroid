package com.lukelorusso.colorblindclick.domain.usecase

import com.lukelorusso.colorblindclick.domain.repository.InfoRepository
import com.lukelorusso.colorblindclick.domain.usecase.base.UseCase

class GetAboutAppUrlUseCase(
    private val repository: InfoRepository
) : UseCase<Unit, String>() {

    override suspend fun run(param: Unit): String {
        return repository.getAboutAppUrl()
    }

}
