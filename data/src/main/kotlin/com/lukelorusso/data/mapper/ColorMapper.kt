package com.lukelorusso.data.mapper

import com.lukelorusso.data.net.dto.ColorResponseDTO
import com.lukelorusso.domain.model.Color

/**
 * Mapper class used to transform [ColorResponseDTO] (in the data layer) to [Color]
 * in the domain layer and vice versa.
 */
class ColorMapper {

    //region DTO to MODEL
    /**
     * Transform a [ColorResponseDTO] into an [Color].
     * @param dto  Object to be transformed.
     * @return [Color] if valid [ColorResponseDTO]
     */
    fun transform(dto: ColorResponseDTO): Color = Color(
        colorHex = dto.rowsColors.first().colorHexCode,
        colorName = dto.rowsColors.first().colorName,
        returnMsg = dto.returnMsg,
        similarity = dto.rowsColors.first().similarityPercentage,
        timestamp = System.currentTimeMillis(),
        originalColor = dto.sourceColorHexCode
    )
    //endregion

}
