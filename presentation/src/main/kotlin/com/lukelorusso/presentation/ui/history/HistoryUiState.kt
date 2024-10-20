package com.lukelorusso.presentation.ui.history

import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.ui.base.ContentState

data class HistoryUiState(
    val contentState: ContentState = ContentState.CONTENT,
    val colorList: List<Color> = emptyList(),
    val isSearchingMode: Boolean = false,
    val searchText: String = ""
)
