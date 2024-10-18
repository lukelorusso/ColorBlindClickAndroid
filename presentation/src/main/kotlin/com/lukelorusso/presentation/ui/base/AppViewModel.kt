package com.lukelorusso.presentation.ui.base

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.*

/**
 * Copyright (C) 2024 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
abstract class AppViewModel<UiState> : ViewModel() {
    abstract val router: AppRouter?

    @Suppress("PropertyName")
    abstract val _uiState: MutableStateFlow<UiState>
    val uiState: StateFlow<UiState>
        get() = _uiState.asStateFlow()

    fun initRouter(activity: Activity, fragment: Fragment?) {
        router?.init(activity, fragment)
    }

    fun initUiState(uiState: UiState) =
        _uiState.update { uiState }

    fun updateUiState(function: (UiState) -> UiState) =
        _uiState.update(function)

    override fun onCleared() {
        super.onCleared()
        router?.clear()
    }
}
