package com.lukelorusso.colorblindclick.data.repository

import com.lukelorusso.colorblindclick.data.datasource.HttpManager
import com.lukelorusso.colorblindclick.data.manager.DatabaseManager
import com.lukelorusso.colorblindclick.data.mapper.SaveDevMapper
import com.lukelorusso.colorblindclick.data.net.RetrofitFactory
import com.lukelorusso.colorblindclick.data.net.api.SaveDevApi
import com.lukelorusso.colorblindclick.domain.entity.ColorEntity
import com.lukelorusso.colorblindclick.domain.repository.SaveDevApiRepository

class SaveDevApiRepositoryImpl(
    private val api: SaveDevApi,
    private val httpManager: HttpManager,
    private val mapper: SaveDevMapper,
    private val databaseManager: DatabaseManager
) : SaveDevApiRepository {

    override suspend fun decodeColorHex(colorHex: String, deviceLanguage: String, deviceUdid: String): ColorEntity {
        val newColor: ColorEntity = httpManager.restCall(
            call = {
                api.getColor(
                    colorHex.removePrefix("#"),
                    deviceLanguage,
                    deviceUdid
                )
            },
            mapper = { mapper.transform(it) }
        )

        databaseManager.getColorList().toMutableList().apply {
            firstOrNull { it.originalColorHex == newColor.originalColorHex }?.let { existent ->
                remove(existent)
            }
            add(0, newColor)
            databaseManager.saveColorList(this)
        }

        return newColor
    }

    override fun getHomeUrl(deviceLanguage: String): String =
        String.format(
            RetrofitFactory.SaveDevApi.WEBSITE_HOME,
            deviceLanguage
        )

    override fun getHelpUrl(deviceLanguage: String): String =
        String.format(
            RetrofitFactory.SaveDevApi.WEBSITE_HELP,
            deviceLanguage
        )
}
