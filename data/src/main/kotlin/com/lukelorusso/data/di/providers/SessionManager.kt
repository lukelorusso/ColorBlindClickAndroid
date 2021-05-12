package com.lukelorusso.data.di.providers

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
class SessionManager(private val sharedPrefProvider: SharedPrefProvider) {

    companion object {
        private const val PREF_LAST_LENS_POSITION = "pref_last_lens_position"
    }

    //region Lens Position
    fun getLastLensPosition(): Int = sharedPrefProvider.get(PREF_LAST_LENS_POSITION, 0)

    fun setLastLensPosition(value: Int) {
        sharedPrefProvider.set(PREF_LAST_LENS_POSITION, value)
    }
    //endregion

}