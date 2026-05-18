package com.lukelorusso.colorblindclick.domain.usecase

import com.lukelorusso.colorblindclick.domain.repository.SettingsRepository
import com.lukelorusso.colorblindclick.domain.usecase.base.UseCase

class MigrateUserDataUseCase(
    private val repository: SettingsRepository
) : UseCase<Unit, Unit>() {

    override suspend fun run(param: Unit) {
        return repository.migratePreferences()
    }

}
