package com.lukelorusso.colorblindclick.data.datasource.impl

import com.lukelorusso.colorblindclick.data.datasource.DatabaseDataSource
import com.lukelorusso.colorblindclick.data.datasource.DatabaseDataSource.Companion.KEY_COLORS
import com.lukelorusso.colorblindclick.domain.usecase.base.Logger
import com.lukelorusso.domain.model.Color

class DatabaseDataSourceTestImpl(private val logger: Logger) : DatabaseDataSource {
    override fun getColorList(): List<Color> = emptyList<Color>().also {
        logger.log { "${this::class.java}: returning fake values for key \"$KEY_COLORS\"" }
    }

    override fun saveColorList(list: List<Color>) =
        logger.log { "${this::class.java}: fake-saving values for key \"$KEY_COLORS\"" }

    override fun clearColorList() =
        logger.log { "${this::class.java}: fake-deleting values for key \"$KEY_COLORS\"" }
}
