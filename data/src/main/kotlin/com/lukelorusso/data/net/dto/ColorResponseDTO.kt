package com.lukelorusso.data.net.dto

import com.google.gson.annotations.SerializedName

/**
 * @author LukeLorusso on 18-12-2018.
 */
data class ColorResponseDTO(
    @SerializedName("sourceColorHexCode") val sourceColorHexCode: String,
    @SerializedName("returnMsg") val returnMsg: String,
    @SerializedName("numRowsColors") val numRowsColors: Int,
    @SerializedName("rowsColors") val rowsColors: List<ColorDetailDTO>,
    @SerializedName("messageToUser") val messageToUser: String,
    @SerializedName("executionTimeSeconds") val executionTimeSeconds: Double
)
