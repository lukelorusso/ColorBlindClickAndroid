package com.lukelorusso.presentation.scenes.camera

import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.scenes.base.view.ContentState
import com.lukelorusso.presentation.scenes.base.view.LoadingState

data class CameraData(
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
        fun createContentOnly() = CameraData(contentState = ContentState.CONTENT)

        fun createLoading() = CameraData(
                loadingState = LoadingState.LOADING,
                contentState = ContentState.CONTENT
        )

        fun createHomeUrl(homeUrl: String) =
                CameraData(contentState = ContentState.CONTENT, homeUrl = homeUrl)

        fun createLastLensPosition(position: Int) =
                CameraData(contentState = ContentState.CONTENT, lastLensPosition = position)

        fun createColor(color: Color) =
                CameraData(contentState = ContentState.CONTENT, color = color)

        fun createSnack(snackMessage: String, isPersistenceException: Boolean? = null) =
                CameraData(
                        contentState = ContentState.CONTENT,
                        snackMessage = snackMessage,
                        isPersistenceException = isPersistenceException
                )
    }

}
