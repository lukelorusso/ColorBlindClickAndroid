package com.lukelorusso.presentation.ui.v3.preview

import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.ui.v3.base.ContentState

data class PreviewUiState(
    val contentState: ContentState = ContentState.CONTENT,
    val color: Color? = null,
    val homeUrl: String = ""
)
