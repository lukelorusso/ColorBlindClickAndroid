package com.lukelorusso.presentation.ui.base

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
abstract class ARenderFragment<Data>(@LayoutRes contentLayoutId: Int = 0) :
    Fragment(contentLayoutId), ADataView<Data> {

    //region RENDER
    protected open fun showLoading(loading: View, visible: Boolean) {
        loading.visibility = if (visible) View.VISIBLE else View.GONE
    }

    protected fun showFullLoading(loadingFull: View, visible: Boolean) {
        loadingFull.visibility = if (visible) View.VISIBLE else View.GONE
    }

    protected fun showRefreshingLoading(swipeRefreshLayout: SwipeRefreshLayout, visible: Boolean) {
        swipeRefreshLayout.isRefreshing = visible
    }

    protected fun showRetryLoading(btnErrorRetry: View, errorProgress: View, visible: Boolean) {
        btnErrorRetry.isClickable = !visible
        errorProgress.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    protected open fun showContent(content: View, visible: Boolean) {
        content.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun showError(viewError: View, visible: Boolean) {
        viewError.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun renderError(textErrorDescription: TextView, messageError: String?) {
        messageError?.also { textErrorDescription.text = it }
    }

    protected open fun renderEvent(event: Event<String>) =
        renderSnack(event.contentIfNotHandled())

    protected open fun renderSnack(messageError: String?) {
        messageError?.also { message ->
            activity?.also { activity ->
                Snackbar.make(
                    activity.findViewById(android.R.id.content),
                    message,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
    //endregion

}
