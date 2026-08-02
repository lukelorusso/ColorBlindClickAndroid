package com.lukelorusso.colorblindclick.presentation.ui.base

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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

    /**
     * Detach the current Activity/Fragment references from the router.
     * Call this from the owning Activity/Fragment's onDestroy() to avoid
     * retaining a stale reference for the (potentially longer-lived)
     * ViewModel between the old owner's destruction and a new owner
     * (re)initializing the router.
     */
    fun clearRouter() {
        router?.clear()
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
