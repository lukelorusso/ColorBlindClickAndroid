package com.lukelorusso.domain.repository

import com.lukelorusso.domain.model.Color

interface HistoryRepository {
    fun getColorList(): List<Color>

    fun deleteColor(color: Color)

    fun deleteAllColors()
}
