package com.lukelorusso.data.repository

import com.lukelorusso.data.datasource.DatabaseDataSource
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.repository.HistoryRepository

class HistoryRepositoryImpl(
    private val databaseDataSource: DatabaseDataSource
) : HistoryRepository {
    override fun getColorList(): List<Color> {
        return databaseDataSource.getColorList()
    }

    override fun deleteColor(color: Color) {
        getColorList()
            .filter { c -> c.originalColorHex() != color.originalColorHex() }
            .let { filteredList -> databaseDataSource.saveColorList(filteredList) }
    }

    override fun deleteAllColors() {
        databaseDataSource.clearColorList()
    }
}
