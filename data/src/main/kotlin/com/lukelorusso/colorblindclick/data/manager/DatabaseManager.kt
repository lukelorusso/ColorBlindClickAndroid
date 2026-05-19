package com.lukelorusso.colorblindclick.data.manager

import com.lukelorusso.colorblindclick.data.datasource.DatabaseDataSource
import com.lukelorusso.domain.model.Color

class DatabaseManager(private val paperDbDataSource: DatabaseDataSource) {
    suspend fun getColorList(): List<Color> =
        paperDbDataSource.getColorList()

    suspend fun saveColorList(list: List<Color>) =
        paperDbDataSource.saveColorList(list)

    suspend fun clearColorList() =
        paperDbDataSource.clearColorList()
}
