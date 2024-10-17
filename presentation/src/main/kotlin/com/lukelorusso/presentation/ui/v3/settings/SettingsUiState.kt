package com.lukelorusso.presentation.ui.v3.settings

import com.lukelorusso.presentation.ui.v3.base.ContentState

data class SettingsUiState(
    val contentState: ContentState = ContentState.CONTENT,
    val pixelNeighbourhood: Int? = null,
    val saveCameraOptions: Boolean? = null
)
