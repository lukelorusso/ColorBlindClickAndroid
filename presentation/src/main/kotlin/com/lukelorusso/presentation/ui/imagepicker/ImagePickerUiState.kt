package com.lukelorusso.presentation.ui.imagepicker

import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.ui.base.ContentState

data class ImagePickerUiState(
    val contentState: ContentState = ContentState.CONTENT,
    val pixelNeighbourhood: Int = -1,
    val color: Color? = null
)
