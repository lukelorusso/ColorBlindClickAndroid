package com.lukelorusso.colorblindclick.data.datasource.impl.roomdb.dao

import androidx.room.*
import com.lukelorusso.colorblindclick.data.datasource.impl.roomdb.dto.ColorDto

@Dao
interface ColorDao {

    companion object {
        internal const val TABLE_NAME = "color"
    }

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setColors(list: List<ColorDto>)

    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getColors(): List<ColorDto>

    @Query("DELETE FROM $TABLE_NAME WHERE timestamp == :timestamp ")
    suspend fun deleteColorByTimestamp(timestamp: Long)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteColors()
}
