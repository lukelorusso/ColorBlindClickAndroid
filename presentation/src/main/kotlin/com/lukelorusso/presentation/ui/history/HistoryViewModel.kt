package com.lukelorusso.presentation.ui.history

import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.usecase.*
import com.lukelorusso.presentation.helper.TrackerHelper
import com.lukelorusso.presentation.ui.base.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val gson: Gson,
    private val trackerHelper: TrackerHelper,
    private val getSavedColorList: GetSavedColorListUseCase,
    private val deleteSavedColor: DeleteSavedColorUseCase,
    private val deleteAllSavedColors: DeleteAllSavedColorsUseCase
) : AppViewModel<HistoryUiState>() {
    override val _uiState = MutableStateFlow(HistoryUiState())
    override val router = HistoryRouter()
    private val loadBouncer = Bouncer(BOUNCE_DELAY_IN_MILLIS)

    init {
        loadData()
    }

    fun loadData() {
        if (uiState.value.contentState.isLoading) {
            return
        } else {
            updateUiState { it.copy(contentState = ContentState.LOADING) }
        }

        viewModelScope.launch {
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
            trackerHelper.track(router.activity, TrackerHelper.Actions.PERSISTENCE_EXCEPTION)
            updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
        }
    }

    fun deleteColorFromUiState(param: Color) {
        updateUiState {
            it.copy(
                colorList = uiState.value.colorList
                    .minus(param) // temporarily preview the result
            )
        }
    }

    fun restoreColorToUiState(param: Color) {
        updateUiState {
            it.copy(
                colorList = uiState.value.colorList
                    .plus(param)
                    .sortedByDescending { color -> color.timestamp }
            )
        }
    }

    fun deleteColor(param: Color) {
        if (uiState.value.contentState.isLoading) {
            return
        } else {
            updateUiState { it.copy(contentState = ContentState.LOADING) }
        }

        viewModelScope.launch {
            try {
                deleteSavedColor.invoke(param)
                trackerHelper.track(router.activity, TrackerHelper.Actions.DELETED_ITEM)
                loadDataSuspend()
            } catch (t: Throwable) {
                trackerHelper.track(router.activity, TrackerHelper.Actions.PERSISTENCE_EXCEPTION)
                updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
            }
        }
    }

    fun deleteAllColors() {
        if (uiState.value.contentState.isLoading) {
            return
        } else {
            updateUiState {
                it.copy(
                    contentState = ContentState.LOADING,
                    colorList = emptyList() // temporarily preview the result
                )
            }
        }

        viewModelScope.launch {
            try {
                deleteAllSavedColors.invoke(Unit)
                trackerHelper.track(router.activity, TrackerHelper.Actions.DELETED_ALL_ITEMS)
                loadDataSuspend()
            } catch (t: Throwable) {
                trackerHelper.track(router.activity, TrackerHelper.Actions.PERSISTENCE_EXCEPTION)
                updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
            }
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
        if (newText.isBlank()) {
            loadBouncer.tick()
            updateUiState { it.copy(searchText = newText) }
        } else {
            // load the job after the defined delay
            loadBouncer.bounce {
                updateUiState { it.copy(searchText = newText) }
            }
        }
    }

    fun gotoPreview(color: Color) =
        router.routeToPreview(gson.toJson(color))

    fun gotoCamera() =
        router.routeToCamera()

    fun dismissError(onDismiss: (() -> Unit)? = null) {
        updateUiState { it.copy(contentState = ContentState.CONTENT) }
        onDismiss?.invoke()
    }

    companion object {
        private const val BOUNCE_DELAY_IN_MILLIS = 250L
    }
}
