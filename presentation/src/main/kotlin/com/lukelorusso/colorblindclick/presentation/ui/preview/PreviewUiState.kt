package com.lukelorusso.colorblindclick.presentation.ui.preview

import com.lukelorusso.colorblindclick.domain.entity.ColorEntity
import com.lukelorusso.colorblindclick.presentation.ui.base.ContentState

data class PreviewUiState(
    val contentState: ContentState = ContentState.CONTENT,
    val color: ColorEntity? = null,
    val storeUrl: String = ""
)
