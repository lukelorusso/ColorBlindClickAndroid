package com.lukelorusso.domain.repository.v3

interface SettingsRepository {

    fun getPixelNeighbourhood(): Int

    fun setPixelNeighbourhood(param: Int)

    fun getSaveCameraOptions(): Boolean

    fun setSaveCameraOptions(param: Boolean)

    fun getLastLensPosition(): Int

    fun setLastLensPosition(position: Int)

    fun getLastZoomValue(): Int

    fun setLastZoomValue(position: Int)

}
