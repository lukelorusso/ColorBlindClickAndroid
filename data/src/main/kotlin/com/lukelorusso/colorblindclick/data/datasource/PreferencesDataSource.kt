package com.lukelorusso.colorblindclick.data.datasource

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
interface PreferencesDataSource {
    suspend fun get(key: String): String?

    suspend fun get(key: String, defValue: String): String

    suspend fun get(key: String, defValue: Int): Int

    suspend fun get(key: String, defValue: Long): Long

    suspend fun get(key: String, defValue: Boolean): Boolean

    suspend fun set(key: String, value: String)

    suspend fun set(key: String, value: Int)

    suspend fun set(key: String, value: Long)

    suspend fun set(key: String, value: Boolean)

    suspend fun delete(key: String)

    suspend fun exist(key: String): Boolean
}
