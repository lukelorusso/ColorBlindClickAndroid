package com.lukelorusso.colorblindclick.data.manager

import com.lukelorusso.colorblindclick.data.datasource.PreferencesDataSource

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
class PreferencesManager(private val preferencesDataSource: PreferencesDataSource) {
    companion object {
        private const val DEFAULT_LAST_LENS_POSITION = 0
        private const val DEFAULT_LAST_ZOOM_VALUE = -1
        private const val DEFAULT_PIXEL_NEIGHBOURHOOD_VALUE = 1
        private const val DEFAULT_SAVE_CAMERA_OPTIONS = true
        private const val PREF_LAST_LENS_POSITION = "pref_last_lens_position"
        private const val PREF_LAST_ZOOM_VALUE = "pref_last_zoom_value"
        private const val PREF_PIXEL_NEIGHBOURHOOD_VALUE = "pref_pixel_neighbourhood_value"
        private const val PREF_SAVE_CAMERA_OPTIONS = "pref_save_camera_options"
    }

    //region Lens Position
    suspend fun loadLastLensPosition(): Int =
        preferencesDataSource.get(PREF_LAST_LENS_POSITION, DEFAULT_LAST_LENS_POSITION)

    suspend fun persistLastLensPosition(value: Int) {
        preferencesDataSource.set(PREF_LAST_LENS_POSITION, value)
    }

    suspend fun deleteLastLensPosition() =
        preferencesDataSource.delete(PREF_LAST_LENS_POSITION)
    //endregion

    //region Zoom Value
    suspend fun loadLastZoomValue(): Int =
        preferencesDataSource.get(PREF_LAST_ZOOM_VALUE, DEFAULT_LAST_ZOOM_VALUE)

    suspend fun persistLastZoomValue(value: Int) {
        preferencesDataSource.set(PREF_LAST_ZOOM_VALUE, value)
    }

    suspend fun deleteLastZoomValue() =
        preferencesDataSource.delete(PREF_LAST_ZOOM_VALUE)
    //endregion

    //region Pixel Neighborhood
    suspend fun loadPixelNeighbourhood(): Int =
        preferencesDataSource.get(PREF_PIXEL_NEIGHBOURHOOD_VALUE, DEFAULT_PIXEL_NEIGHBOURHOOD_VALUE)

    suspend fun persistPixelNeighbourhood(value: Int) {
        preferencesDataSource.set(PREF_PIXEL_NEIGHBOURHOOD_VALUE, value)
    }
    //endregion

    //region Save Camera Options
    suspend fun loadSaveCameraOptions(): Boolean =
        preferencesDataSource.get(PREF_SAVE_CAMERA_OPTIONS, DEFAULT_SAVE_CAMERA_OPTIONS)

    suspend fun persistSaveCameraOptions(shouldSave: Boolean) {
        preferencesDataSource.set(PREF_SAVE_CAMERA_OPTIONS, shouldSave)
    }
    //endregion
}
