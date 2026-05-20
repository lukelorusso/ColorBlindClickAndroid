package com.lukelorusso.colorblindclick.presentation.ui.capture

import com.lukelorusso.colorblindclick.domain.entity.ColorEntity
import com.lukelorusso.colorblindclick.presentation.ui.base.ContentState

data class CaptureUiState(
    val contentState: ContentState = ContentState.CONTENT,
    val lastLensPosition: Int? = null,
    val lastZoomValue: Int? = null,
    val pixelNeighbourhood: Int = -1,
    val color: ColorEntity? = null
)
