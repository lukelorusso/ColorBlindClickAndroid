package com.lukelorusso.data.datasource

import com.lukelorusso.domain.model.Color

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 *
 * PaperDB is affected by a RuntimeException that sometimes pops up:
 * https://github.com/pilgr/Paper/issues/108
 */
interface PersistenceDataSource {
    companion object {
        internal const val KEY_COLORS = "KEY_COLORS"
    }

    //region Color
    fun getColorList(): List<Color>

    fun saveColorList(list: List<Color>)

    fun clearColorList()
    //endregion
}
