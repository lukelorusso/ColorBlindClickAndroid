package com.lukelorusso.colorblindclick.data.manager

import com.lukelorusso.colorblindclick.data.datasource.DatabaseDataSource
import com.lukelorusso.colorblindclick.domain.entity.ColorEntity

class DatabaseManager(private val databaseDataSource: DatabaseDataSource) {
    suspend fun getColorList(): List<ColorEntity> =
        databaseDataSource.getColorList()

    suspend fun saveColorList(list: List<ColorEntity>) =
        databaseDataSource.saveColorList(list)

    suspend fun deleteColor(element: ColorEntity) =
        databaseDataSource.deleteColor(element)

    suspend fun clearColorList() =
        databaseDataSource.clearColorList()
}
