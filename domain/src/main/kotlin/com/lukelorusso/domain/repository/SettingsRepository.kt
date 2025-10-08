package com.lukelorusso.domain.repository

interface SettingsRepository {

    fun getPixelNeighbourhood(): Int

    fun setPixelNeighbourhood(param: Int)

    fun getSaveCameraOptions(): Boolean

    fun setSaveCameraOptions(param: Boolean)

    /**
     * Back camera = 0; Front camera = 1
     */
    fun getLastLensPosition(): Int

    /**
     * First, check if the user wants to save the camera options
     */
    fun setLastLensPosition(position: Int)

    /**
     * Min zoom value = 0; Max zoom value = 100
     */
    fun getLastZoomValue(): Int

    /**
     * First, check if the user wants to save the camera options
     */
    fun setLastZoomValue(position: Int)

    fun getDeviceLanguage(): String

}
