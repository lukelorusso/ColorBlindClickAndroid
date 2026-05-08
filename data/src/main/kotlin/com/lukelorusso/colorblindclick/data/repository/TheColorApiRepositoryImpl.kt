package com.lukelorusso.colorblindclick.data.repository

import com.lukelorusso.colorblindclick.data.datasource.DatabaseDataSource
import com.lukelorusso.colorblindclick.data.datasource.HttpManager
import com.lukelorusso.colorblindclick.data.mapper.TheColorMapper
import com.lukelorusso.colorblindclick.data.net.RetrofitFactory
import com.lukelorusso.colorblindclick.data.net.api.TheColorApi
import com.lukelorusso.colorblindclick.domain.repository.TheColorApiRepository
import com.lukelorusso.domain.model.Color

class TheColorApiRepositoryImpl(
    private val api: TheColorApi,
    private val httpManager: HttpManager,
    private val mapper: TheColorMapper,
    private val databaseDataSource: DatabaseDataSource
) : TheColorApiRepository {

    override suspend fun decodeColorHex(colorHex: String, deviceLanguage: String, deviceUdid: String): Color {
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

    override fun getHomeUrl(deviceLanguage: String): String =
        RetrofitFactory.TheColorApi.WEBSITE

    override fun getHelpUrl(deviceLanguage: String): String =
        RetrofitFactory.TheColorApi.WEBSITE_DOCS
}
