package com.lukelorusso.colorblindclick.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class ColorEntity(
    val colorName: String,
    val matchingColorHex: String,
    val originalColorHex: String,
    val returnMsg: String,
    val similarity: String,
    val timestamp: Long
) {
    /**
     * For searching purposes
     */
    override fun toString(): String {
        return "$colorName #${originalColorHex}"
    }
}
