package com.lukelorusso.presentation.ui.preview

import com.lukelorusso.presentation.ui.base.ContentState

data class PreviewDialogData(
    val contentState: ContentState = ContentState.NONE,
    val homeUrl: String? = null
) {

    companion object {
        fun createHomeUrl(homeUrl: String) =
            PreviewDialogData(contentState = ContentState.CONTENT, homeUrl = homeUrl)

        fun createEmptyContent() = PreviewDialogData(contentState = ContentState.CONTENT)
    }

}
