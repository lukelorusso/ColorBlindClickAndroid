package com.lukelorusso.colorblindclick.data.datasource.impl

import com.lukelorusso.colorblindclick.data.datasource.DatabaseDataSource.Companion.KEY_COLORS
import com.lukelorusso.colorblindclick.domain.usecase.base.Logger
import com.lukelorusso.domain.model.Color
import io.paperdb.Paper

class DatabaseDataSourceImpl(private val logger: Logger) :
    com.lukelorusso.colorblindclick.data.datasource.DatabaseDataSource {

    companion object {
        private const val EXCEPTION_RETRY_TIMES = 3
    }

    //region Color
    override fun getColorList(): List<Color> {
        for (i in 1..EXCEPTION_RETRY_TIMES) {
            try {
                return Paper.book().read<List<Color>>(KEY_COLORS, emptyList()).orEmpty()
            } catch (error: RuntimeException) {
                logger.logError { error }
                if (i == EXCEPTION_RETRY_TIMES) throw RuntimeException("getColorList()", error)
            }
        }
        return emptyList()
    }

    override fun saveColorList(list: List<Color>) {
        for (i in 1..EXCEPTION_RETRY_TIMES) {
            try {
                Paper.book().write(KEY_COLORS, list)
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
