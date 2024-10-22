package com.lukelorusso.data.net.dto

import kotlinx.serialization.Serializable

/**
 * @author LukeLorusso on 18-12-2018.
 */
@Serializable
data class ColorResponseDTO(
    val sourceColorHexCode: String,
    val returnMsg: String,
    val numRowsColors: Int,
    val rowsColors: List<ColorDetailDTO>,
    val messageToUser: String,
    val executionTimeSeconds: Double
)
