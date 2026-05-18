package com.lukelorusso.colorblindclick.data.datasource.impl

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.lukelorusso.colorblindclick.data.datasource.PreferencesDataSource
import com.lukelorusso.colorblindclick.data.datasource.impl.datastore.android.AndroidDataStoreInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStoreDataSourceImpl: PreferencesDataSource {
    private val dataStore = AndroidDataStoreInstance.getInstance().preferences

    override suspend fun get(key: String): String? = dataStore.data.map { prefs ->
        val prefKey = stringPreferencesKey(key)
        prefs[prefKey]
    }.first()

    override suspend fun get(key: String, defValue: String): String = dataStore.data.map { prefs ->
        val prefKey = stringPreferencesKey(key)
        prefs[prefKey]
    }.first() ?: defValue

    override suspend fun get(key: String, defValue: Int): Int = dataStore.data.map { prefs ->
        val prefKey = intPreferencesKey(key)
        prefs[prefKey]
    }.first() ?: defValue

    override suspend fun get(key: String, defValue: Long): Long = dataStore.data.map { prefs ->
        val prefKey = longPreferencesKey(key)
        prefs[prefKey]
    }.first() ?: defValue

    override suspend fun get(key: String, defValue: Boolean): Boolean = dataStore.data.map { prefs ->
        val prefKey = booleanPreferencesKey(key)
        prefs[prefKey]
    }.first() ?: defValue

    override suspend fun set(key: String, value: String) {
        dataStore.edit { prefs ->
            val prefKey = stringPreferencesKey(key)
            prefs[prefKey] = value
        }
    }

    override suspend fun set(key: String, value: Int) {
        dataStore.edit { prefs ->
            val prefKey = intPreferencesKey(key)
            prefs[prefKey] = value
        }
    }

    override suspend fun set(key: String, value: Long) {
        dataStore.edit { prefs ->
            val prefKey = longPreferencesKey(key)
            prefs[prefKey] = value
        }
    }

    override suspend fun set(key: String, value: Boolean) {
        dataStore.edit { prefs ->
            val prefKey = booleanPreferencesKey(key)
            prefs[prefKey] = value
        }
    }

    override suspend fun delete(key: String) {
        dataStore.edit { prefs -> setOfPrefKeys(key).forEach { prefKey -> prefs.remove(prefKey) } }
    }

    override suspend fun exist(key: String): Boolean = dataStore.data.map { prefs ->
        setOfPrefKeys(key).any { prefKey -> prefs.contains(prefKey) }
    }.first()

    private fun setOfPrefKeys(key: String) = setOf(
        stringPreferencesKey(key),
        intPreferencesKey(key),
        longPreferencesKey(key),
        booleanPreferencesKey(key),
        byteArrayPreferencesKey(key),
        doublePreferencesKey(key),
        floatPreferencesKey(key),
        stringSetPreferencesKey(key)
    )
}
