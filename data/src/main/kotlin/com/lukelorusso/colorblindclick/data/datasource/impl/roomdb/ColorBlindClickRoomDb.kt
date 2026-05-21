package com.lukelorusso.colorblindclick.data.datasource.impl.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lukelorusso.colorblindclick.data.BuildConfig
import com.lukelorusso.colorblindclick.data.datasource.impl.roomdb.dao.ColorDao
import com.lukelorusso.colorblindclick.data.datasource.impl.roomdb.dto.ColorDto

@Database(
    entities = [ColorDto::class],
    version = BuildConfig.ROOM_VERSION,
    exportSchema = false
)
abstract class ColorBlindClickRoomDb : RoomDatabase() {
    companion object {
        internal const val DATABASE_NAME = "colorblindclick.db"
    }

    abstract fun colorDao(): ColorDao
}
