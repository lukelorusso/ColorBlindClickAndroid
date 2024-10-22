package com.lukelorusso.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Color(
        var colorHex: String,
        var colorName: String,
        var returnMsg: String,
        var similarity: String,
        var timestamp: Long,
        private val originalColor: String?
) {

    /**
     * For searching purposes
     */
    override fun toString(): String {
        return "$colorName #${originalColorHex()}"
    }

    override fun equals(other: Any?) = other is Color
            && originalColorHex() == other.originalColorHex()

    override fun hashCode(): Int {
        var result = colorHex.hashCode()
        result = 31 * result + colorName.hashCode()
        result = 31 * result + returnMsg.hashCode()
        result = 31 * result + similarity.hashCode()
        result = 31 * result + timestamp.hashCode()
        originalColor?.also { result = 31 * result + it.hashCode() }
        return result
    }

    fun originalColorHex(): String = originalColor ?: colorHex

}
