package com.lukelorusso.colorblindclick.data.datasource.impl

import com.lukelorusso.colorblindclick.data.datasource.DatabaseDataSource
import com.lukelorusso.colorblindclick.domain.entity.ColorEntity
import com.lukelorusso.colorblindclick.domain.usecase.base.Logger
import com.lukelorusso.domain.model.Color
import io.paperdb.Paper

/**
 * PaperDB is affected by a RuntimeException that sometimes pops up:
 * https://github.com/pilgr/Paper/issues/108
 */
class PaperDbDataSourceImpl(private val logger: Logger) : DatabaseDataSource {

    companion object {
        private const val EXCEPTION_RETRY_TIMES = 3
        private const val KEY_COLORS = "KEY_COLORS"

        private fun mapToEntity(paperDbColor: Color): ColorEntity {
            return ColorEntity(
                colorName = paperDbColor.colorName,
                matchingColorHex = paperDbColor.colorHex,
                originalColorHex = paperDbColor.originalColorHex(),
                returnMsg = paperDbColor.returnMsg,
                similarity = paperDbColor.similarity,
                timestamp = paperDbColor.timestamp
            )
        }

        private fun mapToPaperDbColor(colorEntity: ColorEntity): Color {
            return Color(
                colorName = colorEntity.colorName,
                colorHex = colorEntity.matchingColorHex,
                originalColor = colorEntity.originalColorHex,
                returnMsg = colorEntity.returnMsg,
                similarity = colorEntity.similarity,
                timestamp = colorEntity.timestamp
            )
        }
    }

    //region Color
    override fun getColorList(): List<ColorEntity> {
        for (i in 1..EXCEPTION_RETRY_TIMES) {
            try {
                return Paper
                    .book()
                    .read<List<Color>>(KEY_COLORS, emptyList())
                    .orEmpty()
                    .map { paperDbColor -> mapToEntity(paperDbColor) }
            } catch (error: RuntimeException) {
                logger.logError { error }
                if (i == EXCEPTION_RETRY_TIMES) throw RuntimeException("getColorList()", error)
            }
        }
        return emptyList()
    }

    override fun saveColorList(list: List<ColorEntity>) {
        for (i in 1..EXCEPTION_RETRY_TIMES) {
            try {
                Paper.book().write(
                    KEY_COLORS,
                    list.map { colorModel -> mapToPaperDbColor(colorModel) }
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

    override fun clearColorList() {
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
