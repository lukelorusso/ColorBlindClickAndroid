package com.lukelorusso.colorblindclick.presentation.ui.imagepicker

import com.lukelorusso.colorblindclick.presentation.ui.base.ContentState
import com.lukelorusso.domain.model.Color

data class ImagePickerUiState(
    val contentState: ContentState = ContentState.CONTENT,
    val pixelNeighbourhood: Int = -1,
    val color: Color? = null
)
