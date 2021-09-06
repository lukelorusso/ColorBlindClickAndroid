package com.lukelorusso.presentation.ui.camera

import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.ui.base.ContentState

data class CameraData(
    val contentState: ContentState = ContentState.NONE,
    val homeUrl: String? = null,
    val lastLensPosition: Int? = null,
    val lastZoomValue: Int? = null,
    val pixelNeighbourhood: Int? = null,
    val color: Color? = null,
    val isPersistenceException: Boolean? = null
) {

    companion object {
        fun createEmptyContent() = CameraData(contentState = ContentState.CONTENT)

        fun createLoading() = CameraData(contentState = ContentState.LOADING)

        fun createContent(
            homeUrl: String? = null,
            lensPosition: Int? = null,
            zoomValue: Int? = null,
            pixelNeighbourhood: Int? = null
        ) = CameraData(
            contentState = ContentState.CONTENT,
            homeUrl = homeUrl,
            lastLensPosition = lensPosition,
            lastZoomValue = zoomValue,
            pixelNeighbourhood = pixelNeighbourhood
        )

        fun createColor(color: Color) =
            CameraData(contentState = ContentState.CONTENT, color = color)

        fun createIsPersistenceException(isPersistenceException: Boolean? = null) = CameraData(
            contentState = ContentState.CONTENT,
            isPersistenceException = isPersistenceException
        )
    }

}
