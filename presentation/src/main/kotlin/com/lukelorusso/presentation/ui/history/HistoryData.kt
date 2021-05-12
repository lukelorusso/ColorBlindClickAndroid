package com.lukelorusso.presentation.ui.history

import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.ui.base.ContentState

data class HistoryData(
    val contentState: ContentState = ContentState.NONE,
    val list: List<Color>? = null,
    val deletedItem: Color? = null,
    val deletedAllItems: Boolean? = null,
    val isPersistenceException: Boolean? = null
) {

    companion object {
        fun createLoading() = HistoryData(contentState = ContentState.LOADING)

        fun createRetryLoading() = HistoryData(contentState = ContentState.RETRY)

        fun createData(data: List<Color>) =
            HistoryData(contentState = ContentState.CONTENT, list = data)

        fun createDeletedItem(item: Color) =
                HistoryData(contentState = ContentState.CONTENT, deletedItem = item)

        fun createDeletedAllItem() =
                HistoryData(contentState = ContentState.CONTENT, deletedAllItems = true)

        fun createIsPersistenceException(isPersistenceException: Boolean? = null) = HistoryData(
            contentState = ContentState.CONTENT,
            isPersistenceException = isPersistenceException
        )
    }

}
