package com.lukelorusso.presentation.ui.info

import com.lukelorusso.presentation.ui.base.ContentState

data class InfoUiState(
    val contentState: ContentState = ContentState.CONTENT,
    val versionName: String = ""
)
