package com.lukelorusso.presentation.ui.settings

import androidx.lifecycle.viewModelScope
import com.lukelorusso.domain.usecase.*
import com.lukelorusso.presentation.ui.base.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getPixelNeighbourhood: GetPixelNeighbourhoodUseCase,
    private val setPixelNeighbourhood: SetPixelNeighbourhoodUseCase,
    private val getSaveCameraOptions: GetSaveCameraOptionsUseCase,
    private val setSaveCameraOptions: SetSaveCameraOptionsUseCase
) : AppViewModel<SettingsUiState>() {
    override val _uiState = MutableStateFlow(SettingsUiState())
    override val router = object : AppRouter() {} // not needed

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
            val pixelNeighbourhood = getPixelNeighbourhood.invoke(Unit)
            val saveCameraOptions = getSaveCameraOptions.invoke(Unit)
            updateUiState {
                it.copy(
                    contentState = ContentState.CONTENT,
                    pixelNeighbourhood = pixelNeighbourhood,
                    saveCameraOptions = saveCameraOptions
                )
            }
        } catch (t: Throwable) {
            updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
        }
    }

    fun setPixelNeighbourhood(param: Int) {
        if (uiState.value.contentState.isLoading) {
            return
        } else {
            updateUiState { it.copy(contentState = ContentState.LOADING) }
        }

        viewModelScope.launch {
            try {
                setPixelNeighbourhood.invoke(param)
                updateUiState {
                    it.copy(
                        contentState = ContentState.CONTENT,
                        pixelNeighbourhood = param
                    )
                }
            } catch (t: Throwable) {
                updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
            }
        }
    }

    fun setSaveCameraOptions(param: Boolean) {
        if (uiState.value.contentState.isLoading) {
            return
        } else {
            updateUiState { it.copy(contentState = ContentState.LOADING) }
        }

        viewModelScope.launch {
            try {
                setSaveCameraOptions.invoke(param)
                updateUiState {
                    it.copy(
                        contentState = ContentState.CONTENT,
                        saveCameraOptions = param
                    )
                }
            } catch (t: Throwable) {
                updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
            }
        }
    }

    fun dismissError(onDismiss: (() -> Unit)? = null) {
        updateUiState { it.copy(contentState = ContentState.CONTENT) }
        onDismiss?.invoke()
    }
}
