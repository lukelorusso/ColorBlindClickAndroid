package com.lukelorusso.colorblindclick.data.datasource.impl

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.lukelorusso.colorblindclick.data.datasource.PreferencesDataSource
import org.koin.java.KoinJavaComponent.inject

class SharedPrefDataSourceImpl : PreferencesDataSource {
    val context by inject<Context>(Context::class.java)

    private val sharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(context)

    override suspend fun get(key: String): String? =
        if (exist(key)) get(key, "") else null

    override suspend fun get(key: String, defValue: String): String =
        sharedPreferences.getString(key, defValue) ?: defValue

    override suspend fun get(key: String, defValue: Int): Int =
        sharedPreferences.getInt(key, defValue)

    override suspend fun get(key: String, defValue: Long): Long =
        sharedPreferences.getLong(key, defValue)

    override suspend fun get(key: String, defValue: Boolean): Boolean =
        sharedPreferences.getBoolean(key, defValue)

    override suspend fun set(key: String, value: Boolean) =
        sharedPreferences.edit { putBoolean(key, value) }

    override suspend fun set(key: String, value: Int) =
        sharedPreferences.edit { putInt(key, value) }

    override suspend fun set(key: String, value: Long) =
        sharedPreferences.edit { putLong(key, value) }

    override suspend fun set(key: String, value: String) =
        sharedPreferences.edit { putString(key, value) }

    override suspend fun delete(key: String) = sharedPreferences.edit { remove(key) }

    override suspend fun exist(key: String): Boolean = sharedPreferences.contains(key)
}
