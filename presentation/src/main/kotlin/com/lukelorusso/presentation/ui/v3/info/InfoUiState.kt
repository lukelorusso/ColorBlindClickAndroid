package com.lukelorusso.presentation.ui.v3.info

import com.lukelorusso.presentation.ui.v3.base.ContentState

data class InfoUiState(
    val contentState: ContentState = ContentState.CONTENT,
    val versionName: String = ""
)
