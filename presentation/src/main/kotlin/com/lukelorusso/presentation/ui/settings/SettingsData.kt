package com.lukelorusso.presentation.ui.settings

import com.lukelorusso.presentation.ui.base.ContentState

data class SettingsData(
    val contentState: ContentState = ContentState.NONE
) {

    companion object {
        fun createLoading() = SettingsData(contentState = ContentState.LOADING)

        fun createEmptyContent() = SettingsData(contentState = ContentState.CONTENT)
    }

}
