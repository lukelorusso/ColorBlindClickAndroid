package com.lukelorusso.data.di.providers

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
class SessionManager(private val sharedPrefProvider: SharedPrefProvider) {

    companion object {
        private const val PREF_LAST_LENS_POSITION = "pref_last_lens_position"
        private const val PREF_LAST_ZOOM_VALUE = "pref_last_zoom_value"
        private const val PREF_PIXEL_NEIGHBOURHOOD_VALUE = "pref_pixel_neighbourhood_value"
        private const val PREF_SAVE_CAMERA_OPTIONS = "pref_save_camera_options"
        private const val DEFAULT_LAST_LENS_POSITION = 0
        private const val DEFAULT_LAST_ZOOM_VALUE = -1
        private const val DEFAULT_PIXEL_NEIGHBOURHOOD_VALUE = 1
        private const val DEFAULT_SAVE_CAMERA_OPTIONS = true
    }

    //region Lens Position
    fun getLastLensPosition(): Int =
        sharedPrefProvider.get(PREF_LAST_LENS_POSITION, DEFAULT_LAST_LENS_POSITION)

    fun setLastLensPosition(value: Int) {
        sharedPrefProvider.set(PREF_LAST_LENS_POSITION, value)
    }

    fun deleteLastLensPosition() =
        sharedPrefProvider.delete(PREF_LAST_LENS_POSITION)
    //endregion

    //region Zoom Value
    fun getLastZoomValue(): Int =
        sharedPrefProvider.get(PREF_LAST_ZOOM_VALUE, DEFAULT_LAST_ZOOM_VALUE)

    fun setLastZoomValue(value: Int) {
        sharedPrefProvider.set(PREF_LAST_ZOOM_VALUE, value)
    }

    fun deleteLastZoomValue() =
        sharedPrefProvider.delete(PREF_LAST_ZOOM_VALUE)
    //endregion

    //region Pixel Neighbourhood
    fun getPixelNeighbourhood(): Int =
        sharedPrefProvider.get(PREF_PIXEL_NEIGHBOURHOOD_VALUE, DEFAULT_PIXEL_NEIGHBOURHOOD_VALUE)

    fun setPixelNeighbourhood(value: Int) {
        sharedPrefProvider.set(PREF_PIXEL_NEIGHBOURHOOD_VALUE, value)
    }
    //endregion

    //region Save Camera Options
    fun getSaveCameraOptions(): Boolean =
        sharedPrefProvider.get(PREF_SAVE_CAMERA_OPTIONS, DEFAULT_SAVE_CAMERA_OPTIONS)

    fun setSaveCameraOptions(shouldSave: Boolean) {
        sharedPrefProvider.set(PREF_SAVE_CAMERA_OPTIONS, shouldSave)
    }
    //endregion

}
