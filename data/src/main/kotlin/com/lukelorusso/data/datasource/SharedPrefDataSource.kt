package com.lukelorusso.data.datasource

import android.content.Context
import androidx.preference.PreferenceManager
import com.lukelorusso.domain.usecase.base.Logger

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
interface SharedPrefDataSource {
    companion object {
        internal const val DEFAULT_STRING = ""
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
}
