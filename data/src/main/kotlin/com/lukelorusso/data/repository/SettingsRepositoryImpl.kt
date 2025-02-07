package com.lukelorusso.data.repository

import com.lukelorusso.data.datasource.PersistenceManager
import com.lukelorusso.domain.repository.SettingsRepository

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

    /**
     * Back camera = 0; Front camera = 1
     */
    override fun getLastLensPosition(): Int {
        return persistenceManager.loadLastLensPosition()
    }

    /**
     * First, check if the user wants to save the camera options
     */
    override fun setLastLensPosition(position: Int) {
        if (persistenceManager.loadSaveCameraOptions()) {
            persistenceManager.persistLastLensPosition(position)
        }
    }

    /**
     * Min zoom value = 0; Max zoom value = 100
     */
    override fun getLastZoomValue(): Int {
        return persistenceManager.loadLastZoomValue()
    }

    /**
     * First, check if the user wants to save the camera options
     */
    override fun setLastZoomValue(position: Int) {
        if (persistenceManager.loadSaveCameraOptions()) {
            persistenceManager.persistLastZoomValue(position)
        }
    }
}
