package com.lukelorusso.colorblindclick.data.repository

import com.lukelorusso.colorblindclick.data.datasource.HttpManager
import com.lukelorusso.colorblindclick.data.manager.DatabaseManager
import com.lukelorusso.colorblindclick.data.mapper.TheColorMapper
import com.lukelorusso.colorblindclick.data.net.RetrofitFactory
import com.lukelorusso.colorblindclick.data.net.api.TheColorApi
import com.lukelorusso.colorblindclick.domain.entity.ColorEntity
import com.lukelorusso.colorblindclick.domain.repository.TheColorApiRepository

class TheColorApiRepositoryImpl(
    private val api: TheColorApi,
    private val httpManager: HttpManager,
    private val mapper: TheColorMapper,
    private val databaseManager: DatabaseManager
) : TheColorApiRepository {

    override suspend fun decodeColorHex(colorHex: String, deviceLanguage: String, deviceUdid: String): ColorEntity {
        val newColor: ColorEntity = httpManager.restCall(
            call = {
                api.getColor(
                    colorHex.removePrefix("#")
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
        RetrofitFactory.TheColorApi.WEBSITE

    override fun getHelpUrl(deviceLanguage: String): String =
        RetrofitFactory.TheColorApi.WEBSITE_DOCS
}
