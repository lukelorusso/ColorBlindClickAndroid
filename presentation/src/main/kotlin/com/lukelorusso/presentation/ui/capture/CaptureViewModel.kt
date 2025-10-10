package com.lukelorusso.presentation.ui.capture

import androidx.lifecycle.viewModelScope
import com.lukelorusso.domain.usecase.*
import com.lukelorusso.presentation.extensions.getDeviceUdid
import com.lukelorusso.presentation.helper.TrackerHelper
import com.lukelorusso.presentation.ui.base.AppViewModel
import com.lukelorusso.presentation.ui.base.ContentState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import com.lukelorusso.domain.model.Color as ColorEntity

class CaptureViewModel(
    private val trackerHelper: TrackerHelper,
    private val getLastLensPosition: GetLastLensPositionUseCase,
    private val setLastLensPosition: SetLastLensPositionUseCase,
    private val getLastZoomValue: GetLastZoomValueUseCase,
    private val setLastZoomValue: SetLastZoomValueUseCase,
    private val getPixelNeighbourhood: GetPixelNeighbourhoodUseCase,
    private val decodeColorHex: DecodeColorHexUseCase
) : AppViewModel<CaptureUiState>() {
    override val _uiState = MutableStateFlow(CaptureUiState())
    override val router = CaptureRouter()
    private val json = Json { ignoreUnknownKeys = true }

    init {
        loadData()
    }

    private fun loadData() {
        if (uiState.value.contentState.isLoading) {
            return
        } else {
            updateUiState { it.copy(contentState = ContentState.LOADING) }
        }

        viewModelScope.launch {
            try {
                val lastLensPosition = getLastLensPosition.invoke(Unit)
                val lastZoomValue = getLastZoomValue.invoke(Unit)
                val pixelNeighbourhood = getPixelNeighbourhood.invoke(Unit)
                updateUiState {
                    it.copy(
                        contentState = ContentState.CONTENT,
                        lastLensPosition = lastLensPosition,
                        lastZoomValue = lastZoomValue,
                        pixelNeighbourhood = pixelNeighbourhood
                    )
                }
            } catch (t: Throwable) {
                updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
            }
        }
    }

    fun reloadData(lastLensPosition: Int? = null, lastZoomValue: Int? = null) {
        if (uiState.value.contentState.isLoading) {
            return
        }

        viewModelScope.launch {
            try {
                lastLensPosition?.let { setLastLensPosition.invoke(it) }
                lastZoomValue?.let { setLastZoomValue.invoke(it) }
                val pixelNeighbourhood = getPixelNeighbourhood.invoke(Unit)
                updateUiState {
                    it.copy(
                        contentState = ContentState.CONTENT,
                        pixelNeighbourhood = pixelNeighbourhood
                    )
                }
            } catch (t: Throwable) {
                updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
            }
        }
    }

    fun decodeColor(pixelColorHex: String) {
        if (uiState.value.contentState.isLoading) {
            return
        } else {
            updateUiState {
                it.copy(
                    contentState = ContentState.LOADING,
                    color = null
                )
            }
        }

        viewModelScope.launch {
            try {
                val param = DecodeColorHexUseCase.Param(
                    colorHex = pixelColorHex,
                    deviceUdid = router.activity.getDeviceUdid()
                )
                val color = decodeColorHex.invoke(param)
                updateUiState {
                    it.copy(
                        contentState = ContentState.CONTENT,
                        color = color
                    )
                }
            } catch (t: Throwable) {
                trackerHelper.track(TrackerHelper.Action.PERSISTENCE_EXCEPTION)
                updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
            }
        }
    }

    fun setLastLensPosition(param: Int) {
        viewModelScope.launch {
            try {
                setLastLensPosition.invoke(param)
                updateUiState {
                    it.copy(
                        lastLensPosition = param,
                        lastZoomValue = -1
                    )
                }
            } catch (t: Throwable) {
                updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
            }
        }
    }

    fun setLastZoomValue(param: Int) {
        viewModelScope.launch {
            try {
                setLastZoomValue.invoke(param)
                updateUiState { it.copy(lastZoomValue = param) }
            } catch (t: Throwable) {
                updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
            }
        }
    }

    fun dismissErrorAndColor(onDismiss: (() -> Unit)? = null) {
        updateUiState {
            it.copy(
                contentState = ContentState.CONTENT,
                color = null
            )
        }
        onDismiss?.invoke()
    }

    fun gotoInfo() =
        router.routeToInfo()

    fun gotoHistory() =
        router.routeToHistory()

    fun gotoPreview(color: ColorEntity) =
        router.routeToPreview(json.encodeToString<ColorEntity>(color))
}
