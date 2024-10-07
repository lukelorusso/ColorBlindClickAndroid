package com.lukelorusso.data.datasource.impl

import com.lukelorusso.data.datasource.PersistenceDataSource
import com.lukelorusso.data.datasource.PersistenceDataSource.Companion.KEY_COLORS
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.usecase.base.Logger

class PersistenceDataSourceTestImpl(private val logger: Logger) : PersistenceDataSource {
    override fun getColorList(): List<Color> = emptyList<Color>().also {
        logger.log { "${this::class.java}: returning fake values for key \"$KEY_COLORS\"" }
    }

    override fun saveColorList(list: List<Color>) =
        logger.log { "${this::class.java}: fake-saving values for key \"$KEY_COLORS\"" }

    override fun clearColorList() =
        logger.log { "${this::class.java}: fake-deleting values for key \"$KEY_COLORS\"" }
}
