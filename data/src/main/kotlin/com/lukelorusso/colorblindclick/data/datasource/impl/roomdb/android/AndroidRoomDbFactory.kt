package com.lukelorusso.colorblindclick.data.datasource.impl.roomdb.android

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.lukelorusso.colorblindclick.data.datasource.impl.roomdb.ColorBlindClickRoomDb
import com.lukelorusso.colorblindclick.data.datasource.impl.roomdb.ColorBlindClickRoomDb.Companion.DATABASE_NAME
import kotlinx.coroutines.Dispatchers
import org.koin.java.KoinJavaComponent.inject

class AndroidRoomDbFactory {
    val context by inject<Context>(Context::class.java)

    fun createRoomDatabase(): ColorBlindClickRoomDb {
        val file = context.getDatabasePath(DATABASE_NAME)

        return Room
            .databaseBuilder<ColorBlindClickRoomDb>(
                context = context,
                name = file.absolutePath
            )
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .fallbackToDestructiveMigration(false)
            .build()
    }
}
