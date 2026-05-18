package com.lukelorusso.colorblindclick.data.repository

import com.lukelorusso.colorblindclick.data.datasource.PreferencesManager
import com.lukelorusso.colorblindclick.domain.repository.SettingsRepository
import java.util.*

class SettingsRepositoryImpl(
    private val preferencesManager: PreferencesManager
) : SettingsRepository {
    override suspend fun migratePreferences() {
        preferencesManager.migrate()
    }

    override suspend fun getPixelNeighbourhood(): Int {
        return preferencesManager.loadPixelNeighbourhood()
    }

    override suspend fun setPixelNeighbourhood(param: Int) {
        preferencesManager.persistPixelNeighbourhood(param)
    }

    override suspend fun getSaveCameraOptions(): Boolean {
        return preferencesManager.loadSaveCameraOptions()
    }

    override suspend fun setSaveCameraOptions(param: Boolean) {
        preferencesManager.persistSaveCameraOptions(param)

        if (!param) {
            preferencesManager.deleteLastLensPosition()
            preferencesManager.deleteLastZoomValue()
        }
    }

    override suspend fun getLastLensPosition(): Int {
        return preferencesManager.loadLastLensPosition()
    }

    override suspend fun setLastLensPosition(position: Int) {
        if (preferencesManager.loadSaveCameraOptions()) {
            preferencesManager.persistLastLensPosition(position)
        }
    }

    override suspend fun getLastZoomValue(): Int {
        return preferencesManager.loadLastZoomValue()
    }

    override suspend fun setLastZoomValue(position: Int) {
        if (preferencesManager.loadSaveCameraOptions()) {
            preferencesManager.persistLastZoomValue(position)
        }
    }

    override suspend fun getDeviceLanguage(): String {
        val language = Locale.getDefault().language
        if (!APP_SUPPORTED_LANGUAGES.contains(language)) {
            return APP_SUPPORTED_LANGUAGES[0]
        }
        return language
    }
}
