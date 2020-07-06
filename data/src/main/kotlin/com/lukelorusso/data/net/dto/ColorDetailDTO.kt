package com.lukelorusso.data.net.dto

import com.google.gson.annotations.SerializedName

/**
 * @author LukeLorusso on 18-12-2018.
 */
data class ColorDetailDTO(
    @SerializedName("similarityPercentage") val similarityPercentage: String,
    @SerializedName("colorHexCode") val colorHexCode: String,
    @SerializedName("colorName") val colorName: String
)
