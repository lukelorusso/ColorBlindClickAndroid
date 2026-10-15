package com.lukelorusso.colorblindclick.presentation.ui.imagepicker

import androidx.lifecycle.viewModelScope
import com.lukelorusso.colorblindclick.domain.entity.ColorEntity
import com.lukelorusso.colorblindclick.domain.usecase.DecodeColorHexUseCase
import com.lukelorusso.colorblindclick.domain.usecase.GetPixelNeighbourhoodUseCase
import com.lukelorusso.colorblindclick.presentation.extensions.getDeviceUdid
import com.lukelorusso.colorblindclick.presentation.extensions.launchCatching
import com.lukelorusso.colorblindclick.presentation.helper.TrackerHelper
import com.lukelorusso.colorblindclick.presentation.ui.base.AppViewModel
import com.lukelorusso.colorblindclick.presentation.ui.base.ContentState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json

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

        viewModelScope.launchCatching(
            onError = { t ->
                updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
            }
        ) {
            trackerHelper.track(TrackerHelper.Action.GOTO_IMAGE_PICKER)
            val pixelNeighbourhood = getPixelNeighbourhood.invoke(Unit)
            updateUiState {
                it.copy(
                    contentState = ContentState.CONTENT,
                    pixelNeighbourhood = pixelNeighbourhood
                )
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

        viewModelScope.launchCatching(
            onError = { t ->
                trackerHelper.track(TrackerHelper.Action.PERSISTENCE_EXCEPTION)
                updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
            }
        ) {
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

    fun gotoPreview(color: ColorEntity) =
        router.routeToPreview(json.encodeToString<ColorEntity>(color))
}
