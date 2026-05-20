package com.lukelorusso.colorblindclick.data.net.dto

import kotlinx.serialization.Serializable

@Serializable
data class TheColorResponseDTO(
    val hex: TheColorHexDTO,
    val name: TheColorNameDTO
)
