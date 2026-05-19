package com.lukelorusso.colorblindclick.data.repository

import com.lukelorusso.colorblindclick.data.manager.DatabaseManager
import com.lukelorusso.colorblindclick.domain.entity.ColorEntity
import com.lukelorusso.colorblindclick.domain.repository.HistoryRepository

class HistoryRepositoryImpl(
    private val databaseManager: DatabaseManager
) : HistoryRepository {
    override suspend fun getColorList(): List<ColorEntity> {
        return databaseManager.getColorList()
    }

    override suspend fun deleteColor(color: ColorEntity) {
        databaseManager.deleteColor(color)
    }

    override suspend fun deleteAllColors() {
        databaseManager.clearColorList()
    }
}
