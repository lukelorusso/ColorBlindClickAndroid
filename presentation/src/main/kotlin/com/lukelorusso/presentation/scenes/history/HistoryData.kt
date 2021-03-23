package com.lukelorusso.presentation.scenes.history

import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.scenes.base.view.ContentState
import com.lukelorusso.presentation.scenes.base.view.LoadingState

data class HistoryData(
        val loadingState: LoadingState = LoadingState.NONE,
        val contentState: ContentState = ContentState.NONE,
        val list: List<Color>? = null,
        val deletedItem: Color? = null,
        val deletedAllItems: Boolean? = true,
        val errorMessage: String? = null,
        val snackMessage: String? = null,
        val isPersistenceException: Boolean? = true
) {

    companion object {
        fun createLoading() = HistoryData(
                loadingState = LoadingState.LOADING,
                contentState = ContentState.CONTENT
        )

        fun createRetryLoading() =
                HistoryData(loadingState = LoadingState.RETRY, contentState = ContentState.ERROR)

        fun createData(data: List<Color>) =
                HistoryData(contentState = ContentState.CONTENT, list = data)

        fun createDeletedItem(item: Color) =
                HistoryData(contentState = ContentState.CONTENT, deletedItem = item)

        fun createDeletedAllItem() =
                HistoryData(contentState = ContentState.CONTENT, deletedAllItems = true)

        fun createSnack(snackMessage: String, isPersistenceException: Boolean? = null) =
                HistoryData(
                        contentState = ContentState.CONTENT,
                        snackMessage = snackMessage,
                        isPersistenceException = isPersistenceException
                )
    }

}
