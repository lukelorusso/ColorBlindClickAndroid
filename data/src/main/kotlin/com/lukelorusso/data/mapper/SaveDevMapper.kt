package com.lukelorusso.data.mapper

import com.lukelorusso.data.net.dto.SaveDevResponseDTO
import com.lukelorusso.domain.model.Color

/**
 * Mapper class used to transform [SaveDevResponseDTO] (in the data layer) to [Color]
 * in the domain layer and vice versa.
 */
class SaveDevMapper {

    //region DTO to MODEL
    /**
     * Transform a [SaveDevResponseDTO] into an [Color].
     * @param dto  Object to be transformed.
     * @return [Color] if valid [SaveDevResponseDTO]
     */
    fun transform(dto: SaveDevResponseDTO): Color = Color(
        colorHex = dto.rowsColors.first().colorHexCode,
        colorName = dto.rowsColors.first().colorName,
        returnMsg = dto.returnMsg,
        similarity = dto.rowsColors.first().similarityPercentage,
        timestamp = System.currentTimeMillis(),
        originalColor = dto.sourceColorHexCode
    )
    //endregion

}
