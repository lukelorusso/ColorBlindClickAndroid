package com.lukelorusso.colorblindclick.presentation.ui.settings

import androidx.lifecycle.viewModelScope
import com.lukelorusso.colorblindclick.domain.usecase.GetPixelNeighbourhoodUseCase
import com.lukelorusso.colorblindclick.domain.usecase.GetSaveCameraOptionsUseCase
import com.lukelorusso.colorblindclick.domain.usecase.SetPixelNeighbourhoodUseCase
import com.lukelorusso.colorblindclick.domain.usecase.SetSaveCameraOptionsUseCase
import com.lukelorusso.colorblindclick.presentation.extensions.launchCatching
import com.lukelorusso.colorblindclick.presentation.ui.base.AppRouter
import com.lukelorusso.colorblindclick.presentation.ui.base.AppViewModel
import com.lukelorusso.colorblindclick.presentation.ui.base.ContentState
import kotlinx.coroutines.flow.MutableStateFlow

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

        viewModelScope.launchCatching(
            onError = { t ->
                updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
            }
        ) {
            val pixelNeighbourhood = getPixelNeighbourhood.invoke(Unit)
            val saveCameraOptions = getSaveCameraOptions.invoke(Unit)
            updateUiState {
                it.copy(
                    contentState = ContentState.CONTENT,
                    pixelNeighbourhood = pixelNeighbourhood,
                    saveCameraOptions = saveCameraOptions
                )
            }
        }
    }

    fun setPixelNeighbourhood(param: Int) {
        if (uiState.value.contentState.isLoading) {
            return
        } else {
            updateUiState { it.copy(contentState = ContentState.LOADING) }
        }

        viewModelScope.launchCatching(
            onError = { t ->
                updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
            }
        ) {
            setPixelNeighbourhood.invoke(param)
            updateUiState {
                it.copy(
                    contentState = ContentState.CONTENT,
                    pixelNeighbourhood = param
                )
            }
        }
    }

    fun setSaveCameraOptions(param: Boolean) {
        if (uiState.value.contentState.isLoading) {
            return
        } else {
            updateUiState { it.copy(contentState = ContentState.LOADING) }
        }

        viewModelScope.launchCatching(
            onError = { t ->
                updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
            }
        ) {
            setSaveCameraOptions.invoke(param)
            updateUiState {
                it.copy(
                    contentState = ContentState.CONTENT,
                    saveCameraOptions = param
                )
            }
        }
    }

    fun dismissError(onDismiss: (() -> Unit)? = null) {
        updateUiState { it.copy(contentState = ContentState.CONTENT) }
        onDismiss?.invoke()
    }
}
