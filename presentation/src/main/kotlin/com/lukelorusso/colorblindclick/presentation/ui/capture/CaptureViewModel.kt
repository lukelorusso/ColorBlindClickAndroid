package com.lukelorusso.colorblindclick.presentation.ui.capture

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.lukelorusso.colorblindclick.domain.entity.ColorEntity
import com.lukelorusso.colorblindclick.domain.usecase.*
import com.lukelorusso.colorblindclick.presentation.extensions.getDeviceUdid
import com.lukelorusso.colorblindclick.presentation.extensions.launchCatching
import com.lukelorusso.colorblindclick.presentation.helper.TrackerHelper
import com.lukelorusso.colorblindclick.presentation.ui.base.AppViewModel
import com.lukelorusso.colorblindclick.presentation.ui.base.Bouncer
import com.lukelorusso.colorblindclick.presentation.ui.base.ContentState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json

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
    private val zoomValueBouncer = Bouncer()

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
        }
    }

    fun reloadData(lastLensPosition: Int? = null, lastZoomValue: Int? = null) {
        if (uiState.value.contentState.isLoading) {
            return
        }

        viewModelScope.launchCatching(
            onError = { t ->
                updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
            }
        ) {
            lastLensPosition?.let { setLastLensPosition.invoke(it) }
            lastZoomValue?.let { setLastZoomValue.invoke(it) }
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

    fun setLastLensPosition(param: Int) {
        viewModelScope.launchCatching(
            onError = { t ->
                updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
            }
        ) {
            if (uiState.value.contentState.isError) {
                updateUiState { it.copy(contentState = ContentState.CONTENT) }
            }

            setLastLensPosition.invoke(param)
            updateUiState {
                it.copy(
                    lastLensPosition = param,
                    lastZoomValue = -1
                )
            }
        }
    }

    fun setLastZoomValue(param: Int) {
        zoomValueBouncer.bounce(
            scope = viewModelScope,
            onError = { t ->
                updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
            }
        ) {
            if (uiState.value.lastZoomValue != param) {
                setLastZoomValue.invoke(param)
                updateUiState { it.copy(lastZoomValue = param) }
            }
        }
    }

    fun showError(t: Exception) {
        updateUiState {
            it.copy(
                contentState = ContentState.ERROR(t),
                color = null
            )
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

    fun gotoImagePicker(uri: Uri) {
        dismissErrorAndColor()
        router.routeToImagePicker(uri)
    }

    companion object {
        const val LENS_POSITION_BACK = 0
        const val LENS_POSITION_FRONT = 1
    }
}
