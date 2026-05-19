package com.lukelorusso.colorblindclick.data.datasource.impl

import com.lukelorusso.colorblindclick.data.datasource.DatabaseDataSource
import com.lukelorusso.colorblindclick.domain.entity.ColorEntity
import com.lukelorusso.colorblindclick.domain.usecase.base.Logger

class DatabaseMockDataSourceImpl(private val logger: Logger) : DatabaseDataSource {
    override suspend fun getColorList(): List<ColorEntity> = emptyList<ColorEntity>().also {
        logger.log { "${this::class.java}: returning fake values for List<ColorEntity>" }
    }

    override suspend fun saveColorList(list: List<ColorEntity>) =
        logger.log { "${this::class.java}: fake-saving values for List<ColorEntity>" }

    override suspend fun deleteColor(element: ColorEntity) =
        logger.log { "${this::class.java}: fake-deleting ColorEntity" }

    override suspend fun clearColorList() =
        logger.log { "${this::class.java}: fake-deleting values for List<ColorEntity>" }
}
