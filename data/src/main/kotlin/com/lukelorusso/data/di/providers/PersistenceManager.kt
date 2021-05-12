package com.lukelorusso.data.di.providers

import com.lukelorusso.domain.model.Color

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
class PersistenceManager(private val persistenceProvider: PersistenceProvider) {

    companion object {
        private const val KEY_COLORS = "KEY_COLORS"
    }

    //region Color
    fun getColorList(): List<Color> = persistenceProvider.getList(KEY_COLORS)

    fun saveColorList(list: List<Color>) = persistenceProvider.saveList(KEY_COLORS, list)

    fun clearColorList() = persistenceProvider.clearList(KEY_COLORS)
    //endregion

}
