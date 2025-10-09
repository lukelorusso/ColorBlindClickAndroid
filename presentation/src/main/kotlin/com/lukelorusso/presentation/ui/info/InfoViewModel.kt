package com.lukelorusso.presentation.ui.info

import androidx.lifecycle.viewModelScope
import com.lukelorusso.domain.usecase.*
import com.lukelorusso.presentation.BuildConfig
import com.lukelorusso.presentation.helper.TrackerHelper
import com.lukelorusso.presentation.ui.base.AppViewModel
import com.lukelorusso.presentation.ui.base.ContentState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class InfoViewModel(
    private val trackerHelper: TrackerHelper,
    private val getAboutAppUrl: GetAboutAppUrlUseCase,
    private val getApiHomeUrl: GetApiHomeUrlUseCase,
    private val getApiHelpUrl: GetApiHelpUrlUseCase,
    private val getAboutMeUrl: GetAboutMeUrlUseCase
) : AppViewModel<InfoUiState>() {
    override val _uiState = MutableStateFlow(InfoUiState())
    override val router = InfoRouter()

    init {
        loadData()
    }

    private fun loadData() {
        initUiState(
            InfoUiState(
                versionName = "v.${BuildConfig.VERSION_NAME}"
            )
        )
    }

    fun gotoCamera() =
        router.routeToCamera()

    fun gotoAboutApp() {
        dismissError()
        viewModelScope.launch {
            try {
                val url = getAboutAppUrl.invoke(Unit)
                trackerHelper.track(TrackerHelper.Action.GOTO_ABOUT_APP_PAGE)
                router.routeToBrowser(url)
            } catch (t: Throwable) {
                updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
            }
        }
    }

    fun gotoApiHome() {
        dismissError()
        viewModelScope.launch {
            try {
                val url = getApiHomeUrl.invoke(Unit)
                trackerHelper.track(TrackerHelper.Action.GOTO_HOME_PAGE)
                router.routeToBrowser(url)
            } catch (t: Throwable) {
                updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
            }
        }
    }

    fun gotoApiHelp() {
        dismissError()
        viewModelScope.launch {
            try {
                val url = getApiHelpUrl.invoke(Unit)
                trackerHelper.track(TrackerHelper.Action.GOTO_HELP_PAGE)
                router.routeToBrowser(url)
            } catch (t: Throwable) {
                updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
            }
        }
    }

    fun gotoAboutMe() {
        dismissError()
        viewModelScope.launch {
            try {
                val url = getAboutMeUrl.invoke(Unit)
                trackerHelper.track(TrackerHelper.Action.GOTO_ABOUT_ME_PAGE)
                router.routeToBrowser(url)
            } catch (t: Throwable) {
                updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
            }
        }
    }

    fun gotoSettings() {
        trackerHelper.track(TrackerHelper.Action.GOTO_SETTINGS)
        router.routeToSettings()
    }

    fun dismissError(onDismiss: (() -> Unit)? = null) {
        updateUiState { it.copy(contentState = ContentState.CONTENT) }
        onDismiss?.invoke()
    }
}
