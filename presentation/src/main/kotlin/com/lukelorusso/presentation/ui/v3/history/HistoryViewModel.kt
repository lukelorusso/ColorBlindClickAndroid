package com.lukelorusso.presentation.ui.v3.history

import androidx.lifecycle.viewModelScope
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.usecase.v3.*
import com.lukelorusso.presentation.ui.v3.base.AppViewModel
import com.lukelorusso.presentation.ui.v3.base.ContentState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val getSavedColorList: GetSavedColorListUseCase,
    private val deleteSavedColor: DeleteSavedColorUseCase,
    private val deleteAllSavedColors: DeleteAllSavedColorsUseCase
) : AppViewModel<HistoryUiState>() {
    override val _uiState = MutableStateFlow(HistoryUiState())
    override val router = HistoryRouter()

    init {
        loadData()
    }

    fun loadData() {
        if (uiState.value.contentState.isLoading) {
            return
        } else {
            dismissError()
        }

        viewModelScope.launch {
            loadDataSuspend()
        }
    }

    fun deleteColor(param: Color) {
        if (uiState.value.contentState.isLoading) {
            return
        } else {
            dismissError()
        }

        viewModelScope.launch {
            deleteSavedColor.invoke(param)
            loadDataSuspend()
        }
    }

    fun deleteAllColors() {
        if (uiState.value.contentState.isLoading) {
            return
        } else {
            dismissError()
        }

        viewModelScope.launch {
            deleteAllSavedColors.invoke(Unit)
            loadDataSuspend()
        }
    }

    private suspend fun loadDataSuspend() {
        try {
            val colorList = getSavedColorList.invoke(Unit)
            updateUiState {
                it.copy(
                    contentState = ContentState.CONTENT,
                    colorList = colorList
                )
            }
        } catch (t: Throwable) {
            updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
        }
    }

    fun toggleSearchingMode(value: Boolean) {
        updateUiState {
            it.copy(isSearchingMode = value).run {
                if (!value) copy(searchText = "")
                else this
            }
        }

    }

    fun updateSearchText(newText: String) {
        updateUiState { it.copy(searchText = newText) }
    }

    fun gotoPreview(color: Color) =
        router.routeToPreview(color)

    fun gotoCamera() =
        router.routeToCamera()

    fun dismissError(onDismiss: (() -> Unit)? = null) {
        updateUiState { it.copy(contentState = ContentState.CONTENT) }
        onDismiss?.invoke()
    }
}
