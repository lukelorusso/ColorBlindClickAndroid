package com.lukelorusso.presentation.ui.history

import androidx.lifecycle.viewModelScope
import com.lukelorusso.domain.usecase.*
import com.lukelorusso.presentation.helper.TrackerHelper
import com.lukelorusso.presentation.ui.base.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.lukelorusso.domain.model.Color as ColorModel

class HistoryViewModel(
    private val trackerHelper: TrackerHelper,
    private val getSavedColorList: GetSavedColorListUseCase,
    private val deleteSavedColor: DeleteSavedColorUseCase,
    private val deleteAllSavedColors: DeleteAllSavedColorsUseCase
) : AppViewModel<HistoryUiState>() {
    override val _uiState = MutableStateFlow(HistoryUiState())
    override val router = HistoryRouter()
    private val json = Json { ignoreUnknownKeys = true }
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
            trackerHelper.track(TrackerHelper.Actions.PERSISTENCE_EXCEPTION)
            updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
        }
    }

    fun deleteColorFromUiState(param: ColorModel) {
        updateUiState {
            it.copy(
                colorList = uiState.value.colorList
                    .minus(param) // temporarily preview the result
            )
        }
    }

    fun restoreColorToUiState(param: ColorModel) {
        updateUiState {
            it.copy(
                colorList = uiState.value.colorList
                    .plus(param)
                    .sortedByDescending { color -> color.timestamp }
            )
        }
    }

    fun deleteColor(param: ColorModel) {
        if (uiState.value.contentState.isLoading) {
            return
        } else {
            updateUiState { it.copy(contentState = ContentState.LOADING) }
        }

        viewModelScope.launch {
            try {
                deleteSavedColor.invoke(param)
                trackerHelper.track(TrackerHelper.Actions.DELETED_ITEM)
                loadDataSuspend()
            } catch (t: Throwable) {
                trackerHelper.track(TrackerHelper.Actions.PERSISTENCE_EXCEPTION)
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
                trackerHelper.track(TrackerHelper.Actions.DELETED_ALL_ITEMS)
                loadDataSuspend()
            } catch (t: Throwable) {
                trackerHelper.track(TrackerHelper.Actions.PERSISTENCE_EXCEPTION)
                updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
            }
        }
    }

    fun toggleSearchingMode(value: Boolean) {
        updateUiState { it.copy(isSearchingMode = value) }
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

    fun gotoPreview(color: ColorModel) =
        router.routeToPreview(json.encodeToString<ColorModel>(color))

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
