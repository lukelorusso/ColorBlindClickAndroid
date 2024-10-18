package com.lukelorusso.domain.repository

import com.lukelorusso.domain.model.Color

interface ColorRepository {

    suspend fun decodeColorHex(colorHex: String, deviceUdid: String): Color

}
