package com.lukelorusso.presentation.ui.settings

import com.lukelorusso.presentation.ui.base.ContentState

data class SettingsUiState(
    val contentState: ContentState = ContentState.CONTENT,
    val pixelNeighbourhood: Int = 0,
    val saveCameraOptions: Boolean = false
)
