package com.lukelorusso.data.repository

import com.lukelorusso.data.datasource.HttpManager
import com.lukelorusso.data.datasource.PersistenceDataSource
import com.lukelorusso.data.mapper.ColorMapper
import com.lukelorusso.data.net.api.ColorApi
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.repository.ColorRepository
import java.util.*

class ColorRepositoryImpl(
    private val api: ColorApi,
    private val httpManager: HttpManager,
    private val colorMapper: ColorMapper,
    private val persistenceDataSource: PersistenceDataSource
) : ColorRepository {

    override suspend fun decodeColorHex(colorHex: String, deviceUdid: String): Color {
        val newColor: Color = httpManager.restCall(
            call = {
                api.getColor(
                    colorHex.removePrefix("#"),
                    getDeviceLanguage(),
                    deviceUdid
                )
            },
            mapper = { colorMapper.transform(it) }
        )

        persistenceDataSource.getColorList().toMutableList().apply {
            firstOrNull { it.originalColorHex() == newColor.originalColorHex() }?.let { existent ->
                remove(existent)
            }
            add(0, newColor)
            persistenceDataSource.saveColorList(this)
        }

        return newColor
    }

    private fun getDeviceLanguage(): String {
        val language = Locale.getDefault().language
        if (!APP_SUPPORTED_LANGUAGES.contains(language)) {
            return APP_SUPPORTED_LANGUAGES[0]
        }
        return language
    }

}
