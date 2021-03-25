package com.lukelorusso.domain.model

data class Color(
        val originalColor: String,
        var similarColor: String,
        var colorName: String,
        var returnMsg: String,
        var similarity: String,
        var timestamp: Long
) {

    /**
     * For searching purposes
     */
    override fun toString(): String {
        return "$colorName #$similarColor"
    }

    override fun equals(other: Any?) = other is Color && similarColor == other.similarColor

    override fun hashCode(): Int {
        var result = similarColor.hashCode()
        result = 31 * result + colorName.hashCode()
        result = 31 * result + returnMsg.hashCode()
        result = 31 * result + similarity.hashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }

}
