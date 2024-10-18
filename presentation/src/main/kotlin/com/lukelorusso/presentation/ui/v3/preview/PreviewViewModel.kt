package com.lukelorusso.presentation.ui.v3.preview

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.usecase.v3.GetHomeUrlUseCase
import com.lukelorusso.presentation.extensions.*
import com.lukelorusso.presentation.helper.TrackerHelper
import com.lukelorusso.presentation.ui.v3.base.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PreviewViewModel(
    private val gson: Gson,
    private val trackerHelper: TrackerHelper,
    private val getHomeUrl: GetHomeUrlUseCase
) : AppViewModel<PreviewUiState>() {
    override val _uiState = MutableStateFlow(PreviewUiState())
    override val router = object : AppRouter() {} // not needed

    fun loadData(serializedColor: String?) {
        if (uiState.value.contentState.isLoading) {
            return
        } else {
            updateUiState { it.copy(contentState = ContentState.LOADING) }
        }

        viewModelScope.launch {
            try {
                val color = serializedColor?.let { gson.fromJson<Color>(it) }
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
            trackerHelper.track(activity, TrackerHelper.Actions.SHARED_TEXT)
            activity.applicationContext.shareText(text, popupLabel)
        }
    }

    fun shareBitmap(bitmap: Bitmap, description: String, popupLabel: String?) {
        router.activity?.let { activity ->
            trackerHelper.track(activity, TrackerHelper.Actions.SHARED_PREVIEW)
            activity.applicationContext.shareBitmap(bitmap, description, popupLabel)
        }
    }

    fun dismissError(onDismiss: (() -> Unit)? = null) {
        updateUiState { it.copy(contentState = ContentState.CONTENT) }
        onDismiss?.invoke()
    }
}
