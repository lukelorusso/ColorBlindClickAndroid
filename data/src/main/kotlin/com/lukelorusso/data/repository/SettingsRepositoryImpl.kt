package com.lukelorusso.data.repository

import com.lukelorusso.data.datasource.PersistenceManager
import com.lukelorusso.domain.repository.SettingsRepository
import java.util.Locale

class SettingsRepositoryImpl(
    private val persistenceManager: PersistenceManager
) : SettingsRepository {

    override fun getPixelNeighbourhood(): Int {
        return persistenceManager.loadPixelNeighbourhood()
    }

    override fun setPixelNeighbourhood(param: Int) {
        persistenceManager.persistPixelNeighbourhood(param)
    }

    override fun getSaveCameraOptions(): Boolean {
        return persistenceManager.loadSaveCameraOptions()
    }

    override fun setSaveCameraOptions(param: Boolean) {
        persistenceManager.persistSaveCameraOptions(param)

        if (!param) {
            persistenceManager.deleteLastLensPosition()
            persistenceManager.deleteLastZoomValue()
        }
    }

    override fun getLastLensPosition(): Int {
        return persistenceManager.loadLastLensPosition()
    }

    override fun setLastLensPosition(position: Int) {
        if (persistenceManager.loadSaveCameraOptions()) {
            persistenceManager.persistLastLensPosition(position)
        }
    }

    override fun getLastZoomValue(): Int {
        return persistenceManager.loadLastZoomValue()
    }

    override fun setLastZoomValue(position: Int) {
        if (persistenceManager.loadSaveCameraOptions()) {
            persistenceManager.persistLastZoomValue(position)
        }
    }

    override fun getDeviceLanguage(): String {
        val language = Locale.getDefault().language
        if (!APP_SUPPORTED_LANGUAGES.contains(language)) {
            return APP_SUPPORTED_LANGUAGES[0]
        }
        return language
    }
}
