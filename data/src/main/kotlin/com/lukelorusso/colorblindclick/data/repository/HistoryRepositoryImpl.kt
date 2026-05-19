package com.lukelorusso.colorblindclick.data.repository

import com.lukelorusso.colorblindclick.data.manager.DatabaseManager
import com.lukelorusso.colorblindclick.domain.repository.HistoryRepository
import com.lukelorusso.domain.model.Color

class HistoryRepositoryImpl(
    private val databaseManager: DatabaseManager
) : HistoryRepository {
    override suspend fun getColorList(): List<Color> {
        return databaseManager.getColorList()
    }

    override suspend fun deleteColor(color: Color) {
        getColorList()
            .filter { c -> c.originalColorHex() != color.originalColorHex() }
            .let { filteredList -> databaseManager.saveColorList(filteredList) }
    }

    override suspend fun deleteAllColors() {
        databaseManager.clearColorList()
    }
}
