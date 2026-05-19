package com.lukelorusso.colorblindclick.data.mapper

import com.lukelorusso.colorblindclick.data.net.dto.SaveDevResponseDTO
import com.lukelorusso.colorblindclick.domain.entity.ColorEntity

/**
 * Mapper class used to transform [SaveDevResponseDTO] (in the data layer) to [ColorEntity]
 * in the domain layer and vice versa.
 */
class SaveDevMapper {

    //region DTO to MODEL
    /**
     * Transform a [SaveDevResponseDTO] into an [ColorEntity].
     * @param dto  Object to be transformed.
     * @return [ColorEntity] if valid [SaveDevResponseDTO]
     */
    fun transform(dto: SaveDevResponseDTO): ColorEntity {
        val match = dto.rowsColors.first()

        return ColorEntity(
            colorName = match.colorName,
            matchingColorHex = match.colorHexCode,
            originalColorHex = dto.sourceColorHexCode,
            returnMsg = dto.returnMsg,
            similarity = match.similarityPercentage,
            timestamp = System.currentTimeMillis()
        )
    }
    //endregion

}
