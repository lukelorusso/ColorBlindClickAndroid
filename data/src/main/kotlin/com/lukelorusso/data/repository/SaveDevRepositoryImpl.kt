package com.lukelorusso.data.repository

import com.lukelorusso.data.datasource.HttpManager
import com.lukelorusso.data.datasource.DatabaseDataSource
import com.lukelorusso.data.mapper.SaveDevMapper
import com.lukelorusso.data.net.api.SaveDevApi
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.repository.SaveDevRepository

class SaveDevRepositoryImpl(
    private val api: SaveDevApi,
    private val httpManager: HttpManager,
    private val mapper: SaveDevMapper,
    private val databaseDataSource: DatabaseDataSource
) : SaveDevRepository {

    override suspend fun decodeColorHex(colorHex: String, deviceLanguage: String, deviceUdid: String): Color {
        val newColor: Color = httpManager.restCall(
            call = {
                api.getColor(
                    colorHex.removePrefix("#"),
                    deviceLanguage,
                    deviceUdid
                )
            },
            mapper = { mapper.transform(it) }
        )

        databaseDataSource.getColorList().toMutableList().apply {
            firstOrNull { it.originalColorHex() == newColor.originalColorHex() }?.let { existent ->
                remove(existent)
            }
            add(0, newColor)
            databaseDataSource.saveColorList(this)
        }

        return newColor
    }

}
