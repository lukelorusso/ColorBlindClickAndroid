package com.lukelorusso.colorblindclick.data.mapper

import com.lukelorusso.colorblindclick.data.net.dto.TheColorResponseDTO
import com.lukelorusso.colorblindclick.domain.entity.ColorEntity

/**
 * Mapper class used to transform [TheColorResponseDTO] (in the data layer) to [ColorEntity]
 * in the domain layer and vice versa.
 */
class TheColorMapper {

    //region DTO to MODEL
    /**
     * Transform a [TheColorResponseDTO] into an [ColorEntity].
     * @param dto  Object to be transformed.
     * @return [ColorEntity] if valid [TheColorResponseDTO]
     */
    fun transform(dto: TheColorResponseDTO): ColorEntity {
        return ColorEntity(
            colorName = dto.name.value,
            matchingColorHex = dto.name.closest_named_hex,
            originalColorHex = dto.hex.value,
            returnMsg = "Similar color found",
            similarity = dto.name.distance.toString(),
            timestamp = System.currentTimeMillis()
        )
    }
    //endregion

}
