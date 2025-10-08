package com.lukelorusso.domain.repository

import com.lukelorusso.domain.model.Color

interface TheColorRepository {

    suspend fun decodeColorHex(colorHex: String): Color

}
