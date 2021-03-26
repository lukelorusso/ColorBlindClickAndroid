package com.lukelorusso.data.mapper

import com.lukelorusso.data.net.dto.ColorResponseDTO
import com.lukelorusso.domain.model.Color
import javax.inject.Inject

/**
 * Mapper class used to transform [ColorResponseDTO] (in the data layer) to [Color]
 * in the domain layer and vice versa.
 */
class ColorMapper
@Inject constructor() {

    //region DTO to MODEL
    /**
     * Transform a [ColorResponseDTO] into an [Color].
     * @param dto  Object to be transformed.
     * @return [Color] if valid [ColorResponseDTO]
     */
    fun transform(dto: ColorResponseDTO): Color =
            Color(
                    dto.sourceColorHexCode,
                    dto.rowsColors.first().colorHexCode,
                    dto.rowsColors.first().colorName,
                    dto.returnMsg,
                    dto.rowsColors.first().similarityPercentage,
                    System.currentTimeMillis()
            )
    //endregion

}
