package com.lukelorusso.presentation.ui.v3.info

import androidx.lifecycle.viewModelScope
import com.lukelorusso.domain.usecase.v3.*
import com.lukelorusso.presentation.BuildConfig
import com.lukelorusso.presentation.ui.v3.base.AppViewModel
import com.lukelorusso.presentation.ui.v3.base.ContentState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class InfoViewModel(
    private val getHelpUrl: GetHelpUrlUseCase,
    private val getHomeUrl: GetHomeUrlUseCase,
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

    fun gotoHome() {
        dismissError()
        viewModelScope.launch {
            try {
                val url = getHomeUrl.invoke(Unit)
                router.routeToBrowser(url)
            } catch (t: Throwable) {
                updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
            }
        }
    }

    fun gotoHelp() {
        dismissError()
        viewModelScope.launch {
            try {
                val url = getHelpUrl.invoke(Unit)
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
                router.routeToBrowser(url)
            } catch (t: Throwable) {
                updateUiState { it.copy(contentState = ContentState.ERROR(t)) }
            }
        }
    }

    fun gotoSettings() =
        router.routeToSettings()

    fun dismissError(onDismiss: (() -> Unit)? = null) {
        updateUiState { it.copy(contentState = ContentState.CONTENT) }
        onDismiss?.invoke()
    }
}
