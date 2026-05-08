package com.lukelorusso.colorblindclick.presentation.ui.preview

import com.lukelorusso.colorblindclick.presentation.ui.base.ContentState
import com.lukelorusso.domain.model.Color

data class PreviewUiState(
    val contentState: ContentState = ContentState.CONTENT,
    val color: Color? = null,
    val storeUrl: String = ""
)
