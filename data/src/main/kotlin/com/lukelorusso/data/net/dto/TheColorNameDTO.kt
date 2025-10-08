package com.lukelorusso.data.net.dto

import kotlinx.serialization.Serializable

@Serializable
data class TheColorNameDTO(
    val value: String,
    val closest_named_hex: String,
    val distance: Int
)
