package com.lukelorusso.colorblindclick.data.repository

import com.lukelorusso.colorblindclick.data.datasource.DatabaseDataSource
import com.lukelorusso.colorblindclick.domain.repository.HistoryRepository
import com.lukelorusso.domain.model.Color

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
