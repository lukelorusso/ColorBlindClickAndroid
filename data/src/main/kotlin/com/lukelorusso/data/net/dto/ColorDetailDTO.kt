package com.lukelorusso.data.net.dto

import kotlinx.serialization.Serializable

/**
 * @author LukeLorusso on 18-12-2018.
 */
@Serializable
data class ColorDetailDTO(
    val similarityPercentage: String,
    val colorHexCode: String,
    val colorName: String
)
