package com.lukelorusso.colorblindclick.presentation.ui.history

import com.lukelorusso.colorblindclick.presentation.ui.base.ContentState
import com.lukelorusso.domain.model.Color

data class HistoryUiState(
    val contentState: ContentState = ContentState.CONTENT,
    val colorList: List<Color> = emptyList(),
    val isSearchingMode: Boolean = false,
    val searchText: String = ""
)
