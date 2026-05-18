package com.lukelorusso.colorblindclick.domain.repository

interface SettingsRepository {
    suspend fun migratePreferences()

    suspend fun getPixelNeighbourhood(): Int

    suspend fun setPixelNeighbourhood(param: Int)

    suspend fun getSaveCameraOptions(): Boolean

    suspend fun setSaveCameraOptions(param: Boolean)

    /**
     * Back camera = 0; Front camera = 1
     */
    suspend fun getLastLensPosition(): Int

    /**
     * First, check if the user wants to save the camera options
     */
    suspend fun setLastLensPosition(position: Int)

    /**
     * Min zoom value = 0; Max zoom value = 100
     */
    suspend fun getLastZoomValue(): Int

    /**
     * First, check if the user wants to save the camera options
     */
    suspend fun setLastZoomValue(position: Int)

    suspend fun getDeviceLanguage(): String
}
