package com.lukelorusso.colorblindclick.data.datasource.impl

import com.lukelorusso.colorblindclick.data.datasource.DatabaseDataSource
import com.lukelorusso.colorblindclick.domain.usecase.base.Logger
import com.lukelorusso.domain.model.Color

class DatabaseMockDataSourceImpl(private val logger: Logger) : DatabaseDataSource {
    override fun getColorList(): List<Color> = emptyList<Color>().also {
        logger.log { "${this::class.java}: returning fake values for List<Color>" }
    }

    override fun saveColorList(list: List<Color>) =
        logger.log { "${this::class.java}: fake-saving values for List<Color>" }

    override fun clearColorList() =
        logger.log { "${this::class.java}: fake-deleting values for List<Color>" }
}
