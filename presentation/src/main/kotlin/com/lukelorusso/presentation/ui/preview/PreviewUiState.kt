package com.lukelorusso.presentation.ui.preview

import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.ui.base.ContentState

data class PreviewUiState(
    val contentState: ContentState = ContentState.CONTENT,
    val color: Color? = null,
    val homeUrl: String = ""
)
