package com.lukelorusso.presentation.ui.camera

import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.ui.base.ContentState

data class CameraData(
    val contentState: ContentState = ContentState.NONE,
    val homeUrl: String? = null,
    val lastLensPosition: Int? = null,
    val lastZoomValue: Int? = null,
    val color: Color? = null,
    val isPersistenceException: Boolean? = null
) {

    companion object {
        fun createEmptyContent() = CameraData(contentState = ContentState.CONTENT)

        fun createLoading() = CameraData(contentState = ContentState.LOADING)

        fun createHomeUrl(homeUrl: String) =
            CameraData(contentState = ContentState.CONTENT, homeUrl = homeUrl)

        fun createContent(lensPosition: Int? = null, zoomValue: Int? = null) =
            CameraData(
                contentState = ContentState.CONTENT,
                lastLensPosition = lensPosition,
                lastZoomValue = zoomValue
            )

        fun createColor(color: Color) =
            CameraData(contentState = ContentState.CONTENT, color = color)

        fun createIsPersistenceException(isPersistenceException: Boolean? = null) = CameraData(
            contentState = ContentState.CONTENT,
            isPersistenceException = isPersistenceException
        )
    }

}
