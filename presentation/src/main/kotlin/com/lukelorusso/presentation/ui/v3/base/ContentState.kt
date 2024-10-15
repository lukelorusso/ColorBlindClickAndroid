package com.lukelorusso.presentation.ui.v3.base

sealed class ContentState {
    object CONTENT : ContentState()

    object LOADING : ContentState()

    class ERROR(val t: Throwable) : ContentState()

    val isLoading: Boolean
        get() = this is LOADING

    val isError: Boolean
        get() = this is ERROR

    val errorMessage: String?
        get() = (this as? ERROR)?.t?.message
}