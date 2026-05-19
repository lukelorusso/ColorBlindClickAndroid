package com.lukelorusso.colorblindclick.data.datasource.impl

import com.lukelorusso.colorblindclick.data.datasource.PreferencesDataSource
import com.lukelorusso.colorblindclick.domain.usecase.base.Logger

class PreferencesMockDataSourceImpl(private val logger: Logger) : PreferencesDataSource {
    override suspend fun get(key: String): String = "".also {
        logger.log { "${this::class.java}: returning default String for key \"$key\"" }
    }

    override suspend fun get(key: String, defValue: String): String = defValue.also {
        logger.log { "${this::class.java}: returning default String for key \"$key\"" }
    }

    override suspend fun get(key: String, defValue: Int): Int = defValue.also {
        logger.log { "${this::class.java}: returning default Int for key \"$key\"" }
    }

    override suspend fun get(key: String, defValue: Long): Long = defValue.also {
        logger.log { "${this::class.java}: returning default Long for key \"$key\"" }
    }

    override suspend fun get(key: String, defValue: Boolean): Boolean = defValue.also {
        logger.log { "${this::class.java}: returning default Boolean for key \"$key\"" }
    }

    override suspend fun set(key: String, value: String) =
        logger.log { "${this::class.java}: fake-saving String for key \"$key\"" }

    override suspend fun set(key: String, value: Int) =
        logger.log { "${this::class.java}: fake-saving Int for key \"$key\"" }

    override suspend fun set(key: String, value: Long) =
        logger.log { "${this::class.java}: fake-saving Long for key \"$key\"" }

    override suspend fun set(key: String, value: Boolean) =
        logger.log { "${this::class.java}: fake-saving Boolean for key \"$key\"" }

    override suspend fun delete(key: String) =
        logger.log { "${this::class.java}: fake-deleting value for key \"$key\"" }

    override suspend fun exist(key: String): Boolean = false
}
