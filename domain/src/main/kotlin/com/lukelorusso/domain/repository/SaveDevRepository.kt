package com.lukelorusso.domain.repository

import com.lukelorusso.domain.model.Color

interface SaveDevRepository {

    suspend fun decodeColorHex(colorHex: String, deviceLanguage: String, deviceUdid: String): Color

}
