package com.lukelorusso.colorblindclick.presentation.ui.history

import com.lukelorusso.colorblindclick.domain.entity.ColorEntity
import com.lukelorusso.colorblindclick.presentation.ui.base.ContentState

data class HistoryUiState(
    val contentState: ContentState = ContentState.CONTENT,
    val colorList: List<ColorEntity> = emptyList(),
    val tempColorToDelete: ColorEntity? = null,
    val isSearchingMode: Boolean = false,
    val searchText: String = ""
)
