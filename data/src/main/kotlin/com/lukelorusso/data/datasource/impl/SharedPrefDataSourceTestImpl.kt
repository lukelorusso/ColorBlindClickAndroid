package com.lukelorusso.data.datasource.impl

import com.lukelorusso.data.datasource.SharedPrefDataSource
import com.lukelorusso.data.datasource.SharedPrefDataSource.Companion.DEFAULT_STRING
import com.lukelorusso.domain.usecase.base.Logger

class SharedPrefDataSourceTestImpl(private val logger: Logger) : SharedPrefDataSource {
    override fun get(key: String): String = DEFAULT_STRING.also {
        logger.log { "${this::class.java}: returning default String for key \"$key\"" }
    }

    override fun get(key: String, defValue: String): String = defValue.also {
        logger.log { "${this::class.java}: returning default String for key \"$key\"" }
    }

    override fun get(key: String, defValue: Int): Int = defValue.also {
        logger.log { "${this::class.java}: returning default Int for key \"$key\"" }
    }

    override fun get(key: String, defValue: Long): Long = defValue.also {
        logger.log { "${this::class.java}: returning default Long for key \"$key\"" }
    }

    override fun get(key: String, defValue: Boolean): Boolean = defValue.also {
        logger.log { "${this::class.java}: returning default Boolean for key \"$key\"" }
    }

    override fun set(key: String, value: String) =
        logger.log { "${this::class.java}: fake-saving String for key \"$key\"" }

    override fun set(key: String, value: Int) =
        logger.log { "${this::class.java}: fake-saving Int for key \"$key\"" }

    override fun set(key: String, value: Long) =
        logger.log { "${this::class.java}: fake-saving Long for key \"$key\"" }

    override fun set(key: String, value: Boolean) =
        logger.log { "${this::class.java}: fake-saving Boolean for key \"$key\"" }

    override fun delete(key: String) =
        logger.log { "${this::class.java}: fake-deleting value for key \"$key\"" }

    override fun exist(key: String): Boolean = false
}
