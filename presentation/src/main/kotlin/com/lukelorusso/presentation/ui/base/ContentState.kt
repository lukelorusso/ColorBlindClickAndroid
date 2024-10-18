package com.lukelorusso.presentation.ui.base

sealed class ContentState {
    object CONTENT : ContentState()

    object LOADING : ContentState()

    class ERROR(val t: Throwable) : ContentState()

    val isLoading: Boolean
        get() = this is LOADING

    val isError: Boolean
        get() = this is ERROR

    val error: Throwable?
        get() = (this as? ERROR)?.t
}
