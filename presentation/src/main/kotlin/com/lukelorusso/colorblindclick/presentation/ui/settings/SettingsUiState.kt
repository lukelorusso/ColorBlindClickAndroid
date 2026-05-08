package com.lukelorusso.colorblindclick.presentation.ui.settings

import com.lukelorusso.colorblindclick.presentation.ui.base.ContentState

data class SettingsUiState(
    val contentState: ContentState = ContentState.CONTENT,
    val pixelNeighbourhood: Int = 0,
    val saveCameraOptions: Boolean = false
)
