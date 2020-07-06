package com.lukelorusso.data.persistence.dao

import com.lukelorusso.data.helper.TimberWrapper
import com.lukelorusso.domain.model.Color
import io.paperdb.Paper

/**
 * PaperDB is affected by a RuntimeException that sometimes pops up:
 * https://github.com/pilgr/Paper/issues/108
 */
class ColorDAO {

    companion object {
        private const val EXCEPTION_RETRY_TIMES = 3
        const val KEY_COLORS = "KEY_COLORS"
    }

    fun getColorList(): List<Color> {
        for (i in 1..EXCEPTION_RETRY_TIMES) {
            try {
                return Paper.book().read(KEY_COLORS, emptyList())
            } catch (error: RuntimeException) {
                TimberWrapper.e { error }
                if (i == EXCEPTION_RETRY_TIMES) throw RuntimeException("getColorList()", error)
            }
        }
        return emptyList()
    }

    fun saveColorList(list: List<Color>) {
        for (i in 1..EXCEPTION_RETRY_TIMES) {
            try {
                Paper.book().write(KEY_COLORS, list)
                return
            } catch (error: RuntimeException) {
                TimberWrapper.e { error }
                if (i == EXCEPTION_RETRY_TIMES) throw RuntimeException("saveColorList(...)", error)
            }
        }
    }

    fun clearColorList() {
        for (i in 1..EXCEPTION_RETRY_TIMES) {
            try {
                Paper.book().delete(KEY_COLORS)
                return
            } catch (error: RuntimeException) {
                TimberWrapper.e { error }
                if (i == EXCEPTION_RETRY_TIMES) throw RuntimeException("clearColorList()", error)
            }
        }
    }

}
