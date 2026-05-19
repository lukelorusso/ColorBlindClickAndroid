package com.lukelorusso.colorblindclick.data.manager

import com.lukelorusso.colorblindclick.data.datasource.DatabaseDataSource
import com.lukelorusso.colorblindclick.data.datasource.impl.PaperDbDataSourceImpl
import com.lukelorusso.colorblindclick.domain.entity.ColorEntity

class DatabaseManager(private val roomDbDataSource: DatabaseDataSource) {
    suspend fun migrate() {
        val paperDbDataSource = PaperDbDataSourceImpl()

        paperDbDataSource.getColorList().also { colors ->
            if (colors.isNotEmpty()) {
                roomDbDataSource.saveColorList(colors)
                paperDbDataSource.clearColorList()
            }
        }
    }

    suspend fun getColorList(): List<ColorEntity> =
        roomDbDataSource.getColorList()

    suspend fun saveColorList(list: List<ColorEntity>) =
        roomDbDataSource.saveColorList(list)

    suspend fun deleteColor(element: ColorEntity) =
        roomDbDataSource.deleteColor(element)

    suspend fun clearColorList() =
        roomDbDataSource.clearColorList()
}
