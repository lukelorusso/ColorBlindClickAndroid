package com.lukelorusso.data.mapper

import com.lukelorusso.data.net.dto.TheColorResponseDTO
import com.lukelorusso.domain.model.Color

/**
 * Mapper class used to transform [TheColorResponseDTO] (in the data layer) to [Color]
 * in the domain layer and vice versa.
 */
class TheColorMapper {

    //region DTO to MODEL
    /**
     * Transform a [TheColorResponseDTO] into an [Color].
     * @param dto  Object to be transformed.
     * @return [Color] if valid [TheColorResponseDTO]
     */
    fun transform(dto: TheColorResponseDTO): Color = Color(
        colorHex = dto.name.closest_named_hex,
        colorName = dto.name.value,
        returnMsg = "Similar color found",
        similarity = dto.name.distance.toString(),
        timestamp = System.currentTimeMillis(),
        originalColor = dto.hex.value
    )
    //endregion

}
