package com.lukelorusso.presentation.ui.preview

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.lukelorusso.domain.usecase.GetHomeUrlUseCase
import com.lukelorusso.presentation.extensions.shareBitmap
import com.lukelorusso.presentation.extensions.shareText
import com.lukelorusso.presentation.helper.TrackerHelper
import com.lukelorusso.presentation.ui.base.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import com.lukelorusso.domain.model.Color as ColorModel

class PreviewViewModel(
    private val trackerHelper: TrackerHelper,
    private val getHomeUrl: GetHomeUrlUseCase
) : AppViewModel<PreviewUiState>() {
    override val _uiState = MutableStateFlow(PreviewUiState())
    override val router = object : AppRouter() {} // not needed
    private val json = Json { ignoreUnknownKeys = true }

    fun loadData(serializedColor: String?) {
        if (uiState.value.contentState.isLoading) {
            return
        } else {
            updateUiState { it.copy(contentState = ContentState.LOADING) }
        }

        viewModelScope.launch {
            try {
                val color = serializedColor?.let { json.decodeFromString<ColorModel>(it) }
                val url = getHomeUrl.invoke(Unit)
                updateUiState {
                    it.copy(
                        contentState = ContentState.CONTENT,
                        color = color,
                        homeUrl = url
                    )
                }
            } catch (t: Throwable) {
                updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
            }
        }
    }

    fun shareText(text: String, popupLabel: String?) {
        router.activity?.let { activity ->
            activity.applicationContext.shareText(text, popupLabel)
            trackerHelper.track(TrackerHelper.Actions.SHARED_TEXT)
        }
    }

    fun shareBitmap(bitmap: Bitmap, description: String, popupLabel: String?) {
        router.activity?.let { activity ->
            activity.applicationContext.shareBitmap(bitmap, description, popupLabel)
            trackerHelper.track(TrackerHelper.Actions.SHARED_PREVIEW)
        }
    }

    fun dismissError(onDismiss: (() -> Unit)? = null) {
        updateUiState { it.copy(contentState = ContentState.CONTENT) }
        onDismiss?.invoke()
    }
}
