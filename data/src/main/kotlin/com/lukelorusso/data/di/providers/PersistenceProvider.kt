package com.lukelorusso.data.di.providers

import com.lukelorusso.domain.usecase.base.Logger
import io.paperdb.Paper

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 *
 * PaperDB is affected by a RuntimeException that sometimes pops up:
 * https://github.com/pilgr/Paper/issues/108
 */
interface PersistenceProvider {

    fun <Model> getList(key: String): List<Model>

    fun <Model> saveList(key: String, list: List<Model>)

    fun clearList(key: String)

    class Impl(private val logger: Logger) : PersistenceProvider {

        companion object {
            private const val EXCEPTION_RETRY_TIMES = 3
        }

        override fun <Model> getList(key: String): List<Model> {
            for (i in 1..EXCEPTION_RETRY_TIMES) {
                try {
                    return Paper.book().read<List<Model>>(key, emptyList()).orEmpty()
                } catch (error: RuntimeException) {
                    logger.logError { error }
                    if (i == EXCEPTION_RETRY_TIMES) throw RuntimeException("getColorList()", error)
                }
            }
            return emptyList()
        }

        override fun <Model> saveList(key: String, list: List<Model>) {
            for (i in 1..EXCEPTION_RETRY_TIMES) {
                try {
                    Paper.book().write(key, list)
                    return
                } catch (error: RuntimeException) {
                    logger.logError { error }
                    if (i == EXCEPTION_RETRY_TIMES) throw RuntimeException(
                        "saveColorList(...)",
                        error
                    )
                }
            }
        }

        override fun clearList(key: String) {
            for (i in 1..EXCEPTION_RETRY_TIMES) {
                try {
                    Paper.book().delete(key)
                    return
                } catch (error: RuntimeException) {
                    logger.logError { error }
                    if (i == EXCEPTION_RETRY_TIMES) throw RuntimeException(
                        "clearColorList()",
                        error
                    )
                }
            }
        }

    }

    class TestImpl(private val logger: Logger) : PersistenceProvider {

        override fun <Model> getList(key: String): List<Model> = emptyList<Model>().also {
            logger.log { "${this::class.java}: returning fake values for key \"$key\"" }
        }

        override fun <Model> saveList(key: String, list: List<Model>) =
            logger.log { "${this::class.java}: fake-saving values for key \"$key\"" }

        override fun clearList(key: String) =
            logger.log { "${this::class.java}: fake-deleting values for key \"$key\"" }

    }

}
