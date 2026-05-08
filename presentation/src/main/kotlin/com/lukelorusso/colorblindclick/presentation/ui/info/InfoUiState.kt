package com.lukelorusso.colorblindclick.presentation.ui.info

import com.lukelorusso.colorblindclick.presentation.ui.base.ContentState

data class InfoUiState(
    val contentState: ContentState = ContentState.CONTENT,
    val versionName: String = ""
)
