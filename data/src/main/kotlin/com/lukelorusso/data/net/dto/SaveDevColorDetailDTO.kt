package com.lukelorusso.data.net.dto

import kotlinx.serialization.Serializable

@Serializable
data class SaveDevColorDetailDTO(
    val similarityPercentage: String,
    val colorHexCode: String,
    val colorName: String
)
