package com.lukelorusso.colorblindclick.data.datasource.impl

import com.lukelorusso.colorblindclick.data.datasource.DatabaseDataSource
import com.lukelorusso.colorblindclick.domain.entity.ColorEntity
import com.lukelorusso.colorblindclick.domain.usecase.base.Logger
import com.lukelorusso.domain.model.Color
import io.paperdb.Paper
import org.koin.java.KoinJavaComponent.inject

/**
 * PaperDB is affected by a RuntimeException that sometimes pops up:
 * https://github.com/pilgr/Paper/issues/108
 */
class PaperDbDataSourceImpl : DatabaseDataSource {
    val logger by inject<Logger>(Logger::class.java)

    companion object {
        private const val EXCEPTION_RETRY_TIMES = 3
        private const val KEY_COLORS = "KEY_COLORS"

        private fun mapToEntity(dto: Color): ColorEntity {
            return ColorEntity(
                colorName = dto.colorName,
                matchingColorHex = dto.colorHex,
                originalColorHex = dto.originalColorHex(),
                returnMsg = dto.returnMsg,
                similarity = dto.similarity,
                timestamp = dto.timestamp
            )
        }

        private fun mapToDto(entity: ColorEntity): Color {
            return Color(
                colorName = entity.colorName,
                colorHex = entity.matchingColorHex,
                originalColor = entity.originalColorHex,
                returnMsg = entity.returnMsg,
                similarity = entity.similarity,
                timestamp = entity.timestamp
            )
        }
    }

    //region Color
    override suspend fun getColorList(): List<ColorEntity> {
        for (i in 1..EXCEPTION_RETRY_TIMES) {
            try {
                return Paper
                    .book()
                    .read<List<Color>>(KEY_COLORS, emptyList())
                    .orEmpty()
                    .map { dao -> mapToEntity(dao) }
            } catch (error: RuntimeException) {
                logger.logError { error }
                if (i == EXCEPTION_RETRY_TIMES) throw RuntimeException("getColorList()", error)
            }
        }
        return emptyList()
    }

    override suspend fun saveColorList(list: List<ColorEntity>) {
        for (i in 1..EXCEPTION_RETRY_TIMES) {
            try {
                Paper.book().write(
                    KEY_COLORS,
                    list.map { entity -> mapToDto(entity) }
                )
                return
            } catch (error: RuntimeException) {
                logger.logError { error }
                if (i == EXCEPTION_RETRY_TIMES) throw RuntimeException(
                    "saveColorList(...)",
                    error
                )
            }
        }
    }

    override suspend fun deleteColor(element: ColorEntity) {
        getColorList()
            .filter { c -> c.originalColorHex != element.originalColorHex }
            .let { filteredList -> saveColorList(filteredList) }
    }

    override suspend fun clearColorList() {
        for (i in 1..EXCEPTION_RETRY_TIMES) {
            try {
                Paper.book().delete(KEY_COLORS)
                return
            } catch (error: RuntimeException) {
                logger.logError { error }
                if (i == EXCEPTION_RETRY_TIMES) throw RuntimeException(
                    "clearColorList()",
                    error
                )
            }
        }
    }
    //endregion
}
