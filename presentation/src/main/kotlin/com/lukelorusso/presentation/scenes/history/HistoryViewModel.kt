package com.lukelorusso.presentation.scenes.history

import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.scenes.base.view.ContentState
import com.lukelorusso.presentation.scenes.base.view.LoadingState

data class HistoryViewModel(
    val loadingState: LoadingState = LoadingState.NONE,
    val contentState: ContentState = ContentState.NONE,
    val data: List<Color>? = null,
    val deletedItem: Color? = null,
    val deletedAllItems: Boolean? = true,
    val errorMessage: String? = null,
    val snackMessage: String? = null,
    val isPersistenceException: Boolean? = true
) {

    companion object {
        fun createLoading() = HistoryViewModel(
            loadingState = LoadingState.LOADING,
            contentState = ContentState.CONTENT
        )

        fun createRetryLoading() =
            HistoryViewModel(loadingState = LoadingState.RETRY, contentState = ContentState.ERROR)

        fun createData(data: List<Color>) =
            HistoryViewModel(contentState = ContentState.CONTENT, data = data)

        fun createDeletedItem(item: Color) =
            HistoryViewModel(contentState = ContentState.CONTENT, deletedItem = item)

        fun createDeletedAllItem() =
            HistoryViewModel(contentState = ContentState.CONTENT, deletedAllItems = true)

        fun createSnack(snackMessage: String, isPersistenceException: Boolean? = null) =
            HistoryViewModel(
                contentState = ContentState.CONTENT,
                snackMessage = snackMessage,
                isPersistenceException = isPersistenceException
            )
    }

}
