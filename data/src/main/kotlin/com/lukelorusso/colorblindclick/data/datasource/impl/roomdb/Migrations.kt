package com.lukelorusso.colorblindclick.data.datasource.impl.roomdb

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.lukelorusso.colorblindclick.data.datasource.impl.roomdb.dao.ColorDao

/**
 * version 1 -> 2: added the nullable `tag` column to the `color` table (user-defined labels for history entries)
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("ALTER TABLE ${ColorDao.TABLE_NAME} ADD COLUMN tag TEXT")
    }
}
