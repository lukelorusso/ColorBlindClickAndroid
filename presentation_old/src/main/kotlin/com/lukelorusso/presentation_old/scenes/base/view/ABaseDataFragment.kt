package com.lukelorusso.presentation_old.scenes.base.view

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_progress.*

/**
 * Copyright (C) 2020 Mikhael LOPEZ
 * Licensed under the Apache License Version 2.0
 * Base [Fragment] class for data fragments in this application.
 */
abstract class ABaseDataFragment(@LayoutRes contentLayoutId: Int) : ABaseFragment(contentLayoutId) {

    //region RENDER
    protected fun showLoading(visible: Boolean) {
        progress.visibility = if (visible) View.VISIBLE else View.GONE
    }

    protected fun showRefreshingLoading(swipeRefreshLayout: SwipeRefreshLayout, visible: Boolean) {
        swipeRefreshLayout.isRefreshing = visible
    }

    protected fun showRetryLoading(visible: Boolean) {
        btnErrorRetry.isClickable = !visible
        errorProgress.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    protected fun showContent(content: View, visible: Boolean) {
        content.visibility = if (visible) View.VISIBLE else View.GONE
    }

    protected fun showError(visible: Boolean) {
        viewError.visibility = if (visible) View.VISIBLE else View.GONE
    }

    protected fun renderError(messageError: String?) {
        messageError?.also { textErrorDescription.text = it }
    }

    protected fun renderSnack(message: String?) {
        message?.also {
            activity?.also { activity ->
                Snackbar.make(
                    activity.findViewById(android.R.id.content),
                    it, Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
    //endregion

}
