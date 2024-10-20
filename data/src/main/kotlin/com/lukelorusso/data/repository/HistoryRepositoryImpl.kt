package com.lukelorusso.data.repository

import com.lukelorusso.data.datasource.PersistenceDataSource
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.repository.HistoryRepository

class HistoryRepositoryImpl(
    private val persistenceDataSource: PersistenceDataSource
) : HistoryRepository {
    override fun getColorList(): List<Color> {
        return persistenceDataSource.getColorList()
    }

    override fun deleteColor(color: Color) {
        getColorList()
            .filter { c -> c.originalColorHex() != color.originalColorHex() }
            .let { filteredList -> persistenceDataSource.saveColorList(filteredList) }
    }

    override fun deleteAllColors() {
        persistenceDataSource.clearColorList()
    }
}
