package com.lukelorusso.presentation.ui.camera

import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.usecase.*
import com.lukelorusso.presentation.extensions.getDeviceUdid
import com.lukelorusso.presentation.helper.TrackerHelper
import com.lukelorusso.presentation.ui.base.AppViewModel
import com.lukelorusso.presentation.ui.base.ContentState
import io.fotoapparat.capability.Capabilities
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CameraViewModel(
    private val gson: Gson,
    private val trackerHelper: TrackerHelper,
    private val getLastLensPosition: GetLastLensPositionUseCase,
    private val setLastLensPosition: SetLastLensPositionUseCase,
    private val getLastZoomValue: GetLastZoomValueUseCase,
    private val setLastZoomValue: SetLastZoomValueUseCase,
    private val getPixelNeighbourhood: GetPixelNeighbourhoodUseCase,
    private val decodeColorHex: DecodeColorHexUseCase
) : AppViewModel<CameraUiState>() {
    override val _uiState = MutableStateFlow(CameraUiState())
    override val router = CameraRouter()

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

    fun reloadData(lastLensPosition: Int, lastZoomValue: Int) {
        if (uiState.value.contentState.isLoading) {
            return
        }

        viewModelScope.launch {
            try {
                setLastLensPosition.invoke(lastLensPosition)
                setLastZoomValue.invoke(lastZoomValue)
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

    fun decodeColor(pixelColorToHash: String) {
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
                    hex = pixelColorToHash,
                    deviceUdid = router.activity.getDeviceUdid()
                )
                val colorModel = decodeColorHex.invoke(param)
                updateUiState {
                    it.copy(
                        contentState = ContentState.CONTENT,
                        color = colorModel
                    )
                }
            } catch (t: Throwable) {
                trackerHelper.track(router.activity, TrackerHelper.Actions.PERSISTENCE_EXCEPTION)
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

    fun getLastZoomValue() {
        viewModelScope.launch {
            try {
                val lastZoomValue = getLastZoomValue.invoke(Unit)
                updateUiState { it.copy(lastZoomValue = lastZoomValue) }
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

    fun setCameraCapabilities(capabilities: Capabilities?) {
        updateUiState { it.copy(cameraCapabilities = capabilities) }
    }

    fun gotoInfo() =
        router.routeToInfo()

    fun gotoHistory() =
        router.routeToHistory()

    fun gotoPreview(color: Color) =
        router.routeToPreview(gson.toJson(color))
}
