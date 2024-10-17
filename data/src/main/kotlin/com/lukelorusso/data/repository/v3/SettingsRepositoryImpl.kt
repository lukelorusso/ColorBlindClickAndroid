package com.lukelorusso.data.repository.v3

import com.lukelorusso.data.datasource.SettingsManager
import com.lukelorusso.domain.repository.v3.SettingsRepository

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
}
