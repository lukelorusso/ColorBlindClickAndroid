package com.lukelorusso.colorblindclick.domain.usecase

import com.lukelorusso.colorblindclick.domain.repository.HistoryRepository
import com.lukelorusso.colorblindclick.domain.usecase.base.UseCase

class MigrateDatabaseUseCase(
    private val repository: HistoryRepository
) : UseCase<Unit, Unit>() {

    override suspend fun run(param: Unit) {
        return repository.migrateDatabase()
    }

}
