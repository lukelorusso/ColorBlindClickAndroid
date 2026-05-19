package com.lukelorusso.colorblindclick.domain.repository

import com.lukelorusso.domain.model.Color

interface HistoryRepository {
    suspend fun getColorList(): List<Color>

    suspend fun deleteColor(color: Color)

    suspend fun deleteAllColors()
}
