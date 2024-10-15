package com.lukelorusso.presentation.ui.v3.history

import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.ui.v3.base.ContentState

data class HistoryUiState(
    val contentState: ContentState = ContentState.CONTENT,
    val colorList: List<Color> = emptyList(),
    val isSearchingMode: Boolean = false,
    val searchText: String = ""
)
