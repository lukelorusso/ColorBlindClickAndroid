package com.lukelorusso.data.datasource.impl

import android.content.Context
import androidx.preference.PreferenceManager
import com.lukelorusso.data.datasource.SharedPrefDataSource
import com.lukelorusso.data.datasource.SharedPrefDataSource.Companion.DEFAULT_STRING

class SharedPrefDataSourceImpl(private val context: Context) : SharedPrefDataSource {
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
