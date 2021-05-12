package com.lukelorusso.presentation.ui.info

import com.lukelorusso.presentation.ui.base.ContentState

data class InfoData(
    val contentState: ContentState = ContentState.NONE,
    val url: String? = null
) {

    companion object {
        fun createUrlToGoTo(url: String) = InfoData(contentState = ContentState.CONTENT, url = url)

        fun createEmptyContent() = InfoData(contentState = ContentState.CONTENT)
    }

}
