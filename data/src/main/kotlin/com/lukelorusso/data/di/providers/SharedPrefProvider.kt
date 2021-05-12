package com.lukelorusso.data.di.providers

import android.content.Context
import androidx.preference.PreferenceManager
import com.lukelorusso.domain.usecase.base.Logger

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
interface SharedPrefProvider {

    companion object {
        private const val DEFAULT_STRING = ""
    }

    fun get(key: String): String

    fun get(key: String, defValue: String): String

    fun get(key: String, defValue: Int): Int

    fun get(key: String, defValue: Long): Long

    fun get(key: String, defValue: Boolean): Boolean

    fun set(key: String, value: String)

    fun set(key: String, value: Int)

    fun set(key: String, value: Long)

    fun set(key: String, value: Boolean)

    fun delete(key: String)

    fun exist(key: String): Boolean

    class Impl(private val context: Context) : SharedPrefProvider {

        private val sharedPreferences
            get() = PreferenceManager.getDefaultSharedPreferences(context)

        override fun get(key: String): String = get(key, DEFAULT_STRING)

        override fun get(key: String, defValue: String): String =
            sharedPreferences.getString(key, defValue) ?: defValue

        override fun get(key: String, defValue: Int): Int =
            sharedPreferences.getInt(key, defValue)

        override fun get(key: String, defValue: Long): Long =
            sharedPreferences.getLong(key, defValue)

        override fun get(key: String, defValue: Boolean): Boolean =
            sharedPreferences.getBoolean(key, defValue)

        override fun set(key: String, value: Boolean) =
            sharedPreferences.edit().putBoolean(key, value).apply()

        override fun set(key: String, value: Int) =
            sharedPreferences.edit().putInt(key, value).apply()

        override fun set(key: String, value: Long) =
            sharedPreferences.edit().putLong(key, value).apply()

        override fun set(key: String, value: String) =
            sharedPreferences.edit().putString(key, value).apply()

        override fun delete(key: String) = sharedPreferences.edit().remove(key).apply()

        override fun exist(key: String): Boolean = sharedPreferences.contains(key)

    }

    class TestImpl(private val logger: Logger) : SharedPrefProvider {

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

}
