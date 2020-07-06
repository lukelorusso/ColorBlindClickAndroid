package com.lukelorusso.data.di.providers

import android.content.Context
import androidx.preference.PreferenceManager

class SharedPrefProvider(private val context: Context) {

    companion object {
        private const val DEFAULT_STRING = ""
    }

    private val sharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(context)

    fun get(key: String): String = get(key, DEFAULT_STRING)

    fun get(key: String, defValue: String): String =
        sharedPreferences.getString(key, defValue) ?: defValue

    fun get(key: String, defValue: Int): Int =
        sharedPreferences.getInt(key, defValue)

    fun get(key: String, defValue: Long): Long =
        sharedPreferences.getLong(key, defValue)

    fun set(key: String, value: String) =
        sharedPreferences.edit().putString(key, value).apply()

    fun get(key: String, defValue: Boolean): Boolean =
        sharedPreferences.getBoolean(key, defValue)

    fun set(key: String, value: Boolean) =
        sharedPreferences.edit().putBoolean(key, value).apply()

    fun set(key: String, value: Int) =
        sharedPreferences.edit().putInt(key, value).apply()

    fun set(key: String, value: Long) =
        sharedPreferences.edit().putLong(key, value).apply()

    fun delete(key: String) = sharedPreferences.edit().remove(key).apply()

    fun exist(key: String): Boolean = sharedPreferences.contains(key)

}
