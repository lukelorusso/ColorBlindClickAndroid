package com.lukelorusso.colorblindclick.data.datasource.impl.roomdb.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lukelorusso.colorblindclick.data.datasource.impl.roomdb.dao.ColorDao
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = ColorDao.TABLE_NAME)
data class ColorDto(
    val colorName: String,
    val matchingColorHex: String,
    val originalColorHex: String,
    val returnMsg: String,
    val similarity: String,
    val tag: String? = null,
    @PrimaryKey val timestamp: Long
)
