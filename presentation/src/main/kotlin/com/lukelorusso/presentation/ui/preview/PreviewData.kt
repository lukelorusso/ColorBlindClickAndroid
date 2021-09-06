package com.lukelorusso.presentation.ui.preview

import com.lukelorusso.presentation.ui.base.ContentState

data class PreviewData(
    val contentState: ContentState = ContentState.NONE,
    val homeUrl: String? = null
) {

    companion object {
        fun createHomeUrl(homeUrl: String) =
            PreviewData(contentState = ContentState.CONTENT, homeUrl = homeUrl)

        fun createEmptyContent() = PreviewData(contentState = ContentState.CONTENT)
    }

}
