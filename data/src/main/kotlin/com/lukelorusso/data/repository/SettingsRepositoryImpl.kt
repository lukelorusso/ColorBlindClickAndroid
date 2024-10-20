package com.lukelorusso.data.repository

import com.lukelorusso.data.datasource.SettingsManager
import com.lukelorusso.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val settingsManager: SettingsManager
) : SettingsRepository {

    override fun getPixelNeighbourhood(): Int {
        return settingsManager.getPixelNeighbourhood()
    }

    override fun setPixelNeighbourhood(param: Int) {
        settingsManager.setPixelNeighbourhood(param)
    }

    override fun getSaveCameraOptions(): Boolean {
        return settingsManager.getSaveCameraOptions()
    }

    override fun setSaveCameraOptions(param: Boolean) {
        settingsManager.setSaveCameraOptions(param)

        if (!param) {
            settingsManager.deleteLastLensPosition()
            settingsManager.deleteLastZoomValue()
        }
    }

    /**
     * Back camera = 0; Front camera = 1
     */
    override fun getLastLensPosition(): Int {
        return settingsManager.getLastLensPosition()
    }

    /**
     * First, check if the user wants to save the camera options
     */
    override fun setLastLensPosition(position: Int) {
        if (settingsManager.getSaveCameraOptions()) {
            settingsManager.setLastLensPosition(position)
        }
    }

    /**
     * Min zoom value = 0; Max zoom value = 100
     */
    override fun getLastZoomValue(): Int {
        return settingsManager.getLastZoomValue()
    }

    /**
     * First, check if the user wants to save the camera options
     */
    override fun setLastZoomValue(position: Int) {
        if (settingsManager.getSaveCameraOptions()) {
            settingsManager.setLastZoomValue(position)
        }
    }
}
