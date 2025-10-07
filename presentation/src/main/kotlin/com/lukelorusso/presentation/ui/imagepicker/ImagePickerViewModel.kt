package com.lukelorusso.presentation.ui.imagepicker

import androidx.lifecycle.viewModelScope
import com.lukelorusso.domain.usecase.DecodeColorHexUseCase
import com.lukelorusso.domain.usecase.GetPixelNeighbourhoodUseCase
import com.lukelorusso.presentation.extensions.getDeviceUdid
import com.lukelorusso.presentation.helper.TrackerHelper
import com.lukelorusso.presentation.ui.base.AppViewModel
import com.lukelorusso.presentation.ui.base.ContentState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import com.lukelorusso.domain.model.Color as ColorModel

class ImagePickerViewModel(
    private val trackerHelper: TrackerHelper,
    private val getPixelNeighbourhood: GetPixelNeighbourhoodUseCase,
    private val decodeColorHex: DecodeColorHexUseCase
) : AppViewModel<ImagePickerUiState>() {
    override val _uiState = MutableStateFlow(ImagePickerUiState())
    override val router = ImagePickerRouter()
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
                trackerHelper.track(TrackerHelper.Action.GOTO_IMAGE_PICKER)
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
                trackerHelper.track(TrackerHelper.Action.PERSISTENCE_EXCEPTION)
                updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
            }
        }
    }

    fun setError(exception: Exception) {
        updateUiState {
            it.copy(
                contentState = ContentState.ERROR(exception),
                color = null
            )
        }
    }

    fun gotoPreview(color: ColorModel) =
        router.routeToPreview(json.encodeToString<ColorModel>(color))
}
