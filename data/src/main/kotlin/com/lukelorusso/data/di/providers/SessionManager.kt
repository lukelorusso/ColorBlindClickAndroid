package com.lukelorusso.data.di.providers

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
class SessionManager(private val sharedPrefProvider: SharedPrefProvider) {

    companion object {
        private const val PREF_PIXEL_NEIGHBOURHOOD_VALUE = "pref_pixel_neighbourhood_value"
        private const val PREF_LAST_LENS_POSITION = "pref_last_lens_position"
        private const val PREF_LAST_ZOOM_VALUE = "pref_last_zoom_value"
    }

    //region Pixel Neighbourhood
    fun getPixelNeighbourhood(): Int =
        sharedPrefProvider.get(PREF_PIXEL_NEIGHBOURHOOD_VALUE, 1)

    fun setPixelNeighbourhood(value: Int) {
        sharedPrefProvider.set(PREF_PIXEL_NEIGHBOURHOOD_VALUE, value)
    }
    //endregion

    //region Lens Position
    fun getLastLensPosition(): Int =
        sharedPrefProvider.get(PREF_LAST_LENS_POSITION, 0)

    fun setLastLensPosition(value: Int) {
        sharedPrefProvider.set(PREF_LAST_LENS_POSITION, value)
    }
    //endregion

    //region Zoom Value
    fun getLastZoomValue(): Int =
        sharedPrefProvider.get(PREF_LAST_ZOOM_VALUE, -1)

    fun setLastZoomValue(value: Int) {
        sharedPrefProvider.set(PREF_LAST_ZOOM_VALUE, value)
    }

    fun deleteLastZoomValue() =
        sharedPrefProvider.delete(PREF_LAST_ZOOM_VALUE)
    //endregion

}
