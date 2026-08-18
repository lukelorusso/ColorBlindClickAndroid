package com.lukelorusso.colorblindclick.data.datasource.impl

import com.lukelorusso.colorblindclick.data.datasource.DatabaseDataSource
import com.lukelorusso.colorblindclick.data.datasource.impl.roomdb.ColorBlindClickRoomDb
import com.lukelorusso.colorblindclick.data.datasource.impl.roomdb.android.AndroidRoomDbFactory
import com.lukelorusso.colorblindclick.data.datasource.impl.roomdb.dto.ColorDto
import com.lukelorusso.colorblindclick.domain.entity.ColorEntity

class RoomDbDataSourceImpl : DatabaseDataSource {
    companion object {
        private fun mapToEntity(dto: ColorDto): ColorEntity {
            return ColorEntity(
                colorName = dto.colorName,
                matchingColorHex = dto.matchingColorHex,
                originalColorHex = dto.originalColorHex,
                returnMsg = dto.returnMsg,
                similarity = dto.similarity,
                tag = dto.tag,
                timestamp = dto.timestamp
            )
        }

        private fun mapToDto(entity: ColorEntity): ColorDto {
            return ColorDto(
                colorName = entity.colorName,
                matchingColorHex = entity.matchingColorHex,
                originalColorHex = entity.originalColorHex,
                returnMsg = entity.returnMsg,
                similarity = entity.similarity,
                tag = entity.tag,
                timestamp = entity.timestamp
            )
        }
    }

    private val database: ColorBlindClickRoomDb = AndroidRoomDbFactory().createRoomDatabase()

    override suspend fun getColorList(): List<ColorEntity> =
        database.colorDao().getColors().map { dto -> mapToEntity(dto) }

    override suspend fun saveColorList(list: List<ColorEntity>) =
        database.colorDao().setColors(list.map { entity -> mapToDto(entity) })

    override suspend fun deleteColor(element: ColorEntity) =
        database.colorDao().deleteColorByTimestamp(element.timestamp)

    override suspend fun clearColorList() =
        database.colorDao().deleteColors()
}
