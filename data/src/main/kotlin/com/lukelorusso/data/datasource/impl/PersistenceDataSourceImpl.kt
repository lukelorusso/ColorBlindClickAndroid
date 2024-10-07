package com.lukelorusso.data.datasource.impl

import com.lukelorusso.data.datasource.PersistenceDataSource
import com.lukelorusso.data.datasource.PersistenceDataSource.Companion.KEY_COLORS
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.usecase.base.Logger
import io.paperdb.Paper

class PersistenceDataSourceImpl(private val logger: Logger) : PersistenceDataSource {

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
