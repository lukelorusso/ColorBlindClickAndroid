package com.lukelorusso.colorblindclick.data.manager

import com.lukelorusso.colorblindclick.data.datasource.DatabaseDataSource
import com.lukelorusso.colorblindclick.domain.entity.ColorEntity

class DatabaseManager(private val paperDbDataSource: DatabaseDataSource) {
    suspend fun getColorList(): List<ColorEntity> =
        paperDbDataSource.getColorList()

    suspend fun saveColorList(list: List<ColorEntity>) =
        paperDbDataSource.saveColorList(list)

    suspend fun clearColorList() =
        paperDbDataSource.clearColorList()
}
