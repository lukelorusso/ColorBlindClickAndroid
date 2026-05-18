package com.lukelorusso.colorblindclick.data.datasource.impl.datastore.android

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath
import org.koin.java.KoinJavaComponent.inject

class AndroidDataStoreInstance {
    val context by inject<Context>(Context::class.java)

    val preferences = createDataStore {
        context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
    }

    private fun createDataStore(producePath: () -> String): DataStore<Preferences> {
        return PreferenceDataStoreFactory.createWithPath(produceFile = {
            producePath().toPath()
        })
    }

    companion object {
        private const val DATA_STORE_FILE_NAME =
            "com.lukelorusso.colorblindclick.preferences_pb"

        @Volatile
        private var instance: AndroidDataStoreInstance? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: AndroidDataStoreInstance().also { instance = it }
            }
    }
}
