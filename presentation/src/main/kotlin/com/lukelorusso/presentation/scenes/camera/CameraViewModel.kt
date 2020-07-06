package com.lukelorusso.presentation.scenes.camera

import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.scenes.base.view.ContentState
import com.lukelorusso.presentation.scenes.base.view.LoadingState

data class CameraViewModel(
    val loadingState: LoadingState = LoadingState.NONE,
    val contentState: ContentState = ContentState.NONE,
    val homeUrl: String? = null,
    val lastLensPosition: Int? = null,
    val color: Color? = null,
    val errorMessage: String? = null,
    val snackMessage: String? = null,
    val isPersistenceException: Boolean? = true
) {

    companion object {
        fun createContentOnly() = CameraViewModel(contentState = ContentState.CONTENT)

        fun createLoading() = CameraViewModel(
            loadingState = LoadingState.LOADING,
            contentState = ContentState.CONTENT
        )

        fun createHomeUrl(homeUrl: String) =
            CameraViewModel(contentState = ContentState.CONTENT, homeUrl = homeUrl)

        fun createLastLensPosition(position: Int) =
            CameraViewModel(contentState = ContentState.CONTENT, lastLensPosition = position)

        fun createColor(color: Color) =
            CameraViewModel(contentState = ContentState.CONTENT, color = color)

        fun createSnack(snackMessage: String, isPersistenceException: Boolean? = null) =
            CameraViewModel(
                contentState = ContentState.CONTENT,
                snackMessage = snackMessage,
                isPersistenceException = isPersistenceException
            )
    }

}
