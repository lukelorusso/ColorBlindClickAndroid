package com.lukelorusso.domain.model

data class Color(
    var colorHex: String,
    var colorName: String,
    var returnMsg: String,
    var similarity: String,
    var timestamp: Long
) {

    /**
     * For searching purposes
     */
    override fun toString(): String {
        return "$colorName #$colorHex"
    }

    override fun equals(other: Any?) = other is Color && colorHex == other.colorHex

    override fun hashCode(): Int {
        var result = colorHex.hashCode()
        result = 31 * result + colorName.hashCode()
        result = 31 * result + returnMsg.hashCode()
        result = 31 * result + similarity.hashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }

}
