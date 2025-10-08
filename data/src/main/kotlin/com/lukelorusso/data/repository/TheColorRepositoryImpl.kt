package com.lukelorusso.data.repository

import com.lukelorusso.data.datasource.DatabaseDataSource
import com.lukelorusso.data.datasource.HttpManager
import com.lukelorusso.data.mapper.TheColorMapper
import com.lukelorusso.data.net.api.TheColorApi
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.repository.TheColorRepository

class TheColorRepositoryImpl(
    private val api: TheColorApi,
    private val httpManager: HttpManager,
    private val mapper: TheColorMapper,
    private val databaseDataSource: DatabaseDataSource
) : TheColorRepository {

    override suspend fun decodeColorHex(colorHex: String): Color {
        val newColor: Color = httpManager.restCall(
            call = {
                api.getColor(
                    colorHex.removePrefix("#")
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
