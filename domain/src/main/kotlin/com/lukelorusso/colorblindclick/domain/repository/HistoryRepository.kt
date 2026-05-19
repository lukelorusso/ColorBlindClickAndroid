package com.lukelorusso.colorblindclick.domain.repository

import com.lukelorusso.colorblindclick.domain.entity.ColorEntity

interface HistoryRepository {
    suspend fun getColorList(): List<ColorEntity>

    suspend fun deleteColor(color: ColorEntity)

    suspend fun deleteAllColors()
}
