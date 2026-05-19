package com.lukelorusso.colorblindclick.presentation.ui.imagepicker

import com.lukelorusso.colorblindclick.domain.entity.ColorEntity
import com.lukelorusso.colorblindclick.presentation.ui.base.ContentState

data class ImagePickerUiState(
    val contentState: ContentState = ContentState.CONTENT,
    val pixelNeighbourhood: Int = -1,
    val color: ColorEntity? = null
)
