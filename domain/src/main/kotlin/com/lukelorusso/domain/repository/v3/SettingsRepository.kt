package com.lukelorusso.domain.repository.v3

interface SettingsRepository {

    fun getPixelNeighbourhood(): Int

    fun setPixelNeighbourhood(param: Int)

    fun getSaveCameraOptions(): Boolean

    fun setSaveCameraOptions(param: Boolean)

}
