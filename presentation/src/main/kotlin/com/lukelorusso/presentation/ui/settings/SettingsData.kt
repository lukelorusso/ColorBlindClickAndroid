package com.lukelorusso.presentation.ui.settings

import com.lukelorusso.presentation.ui.base.ContentState

data class SettingsData(
    val contentState: ContentState = ContentState.NONE,
    val pixelNeighbourhood: Int? = null
) {

    companion object {
        fun createContent(pixelNeighbourhood: Int? = null) = SettingsData(
            contentState = ContentState.CONTENT,
            pixelNeighbourhood = pixelNeighbourhood
        )

        fun createEmptyContent() = SettingsData(contentState = ContentState.CONTENT)
    }

}
