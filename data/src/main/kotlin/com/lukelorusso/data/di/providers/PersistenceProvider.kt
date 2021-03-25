package com.lukelorusso.data.di.providers

import com.lukelorusso.data.persistence.AppDatabase
import com.lukelorusso.domain.model.Color

/**
 * Copyright (C) 2020 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 * Exposing methods to get, set and delete persistence data.
 */
class PersistenceProvider(private val appDatabase: AppDatabase) {

    fun getColorList() = appDatabase.color().getColorList()

    fun saveColor(newColor: Color) =
        appDatabase.color().getColorList().toMutableList().apply {
            val existent = find { c -> c.similarColor == newColor.similarColor }
            existent?.also { remove(it) }
            add(0, newColor)
            appDatabase.color().saveColorList(this)
        }

    fun deleteColor(color: Color) =
        appDatabase.color().getColorList().toMutableList().apply {
            val existent = find { c -> c.similarColor == color.similarColor }
            existent?.also { remove(it) }
            appDatabase.color().saveColorList(this)
        }

    fun deleteAllColors() = appDatabase.color().clearColorList()

}
