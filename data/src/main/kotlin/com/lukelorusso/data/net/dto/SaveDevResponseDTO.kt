package com.lukelorusso.data.net.dto

import kotlinx.serialization.Serializable

@Serializable
data class SaveDevResponseDTO(
    val sourceColorHexCode: String,
    val returnMsg: String,
    val numRowsColors: Int,
    val rowsColors: List<SaveDevColorDetailDTO>,
    val messageToUser: String,
    val executionTimeSeconds: Double
)
