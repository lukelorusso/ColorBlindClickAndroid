package com.lukelorusso.presentation.ui.base

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.lukelorusso.presentation.R


/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 *
 * Base [BottomSheetDialogFragment] class for every fragment in this application.
 */
abstract class ARenderBottomSheetDialogFragment<Data>(
    private val isFullScreen: Boolean = false
) : BottomSheetDialogFragment(), ADataView<Data> {

    //region RENDER
    protected open fun showLoading(loading: View, visible: Boolean) {
        loading.visibility = if (visible) View.VISIBLE else View.GONE
    }

    protected fun showRefreshingLoading(swipeRefreshLayout: SwipeRefreshLayout, visible: Boolean) {
        swipeRefreshLayout.isRefreshing = visible
    }

    protected open fun showContent(content: View, visible: Boolean) {
        content.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun showError(viewError: View, visible: Boolean) {
        viewError.visibility = if (visible) View.VISIBLE else View.GONE
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

    override fun getTheme(): Int = R.style.BottomSheetDialog

    protected val behavior by lazy {
        (dialog as BottomSheetDialog).behavior
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isFullScreen) {
            view.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    dialog?.findViewById<View>(R.id.design_bottom_sheet)?.apply {
                        layoutParams = layoutParams.apply {
                            height =
                                ViewGroup.LayoutParams.MATCH_PARENT
                        }
                    }

                    with(behavior) {
                        isFitToContents = false
                        state =
                            BottomSheetBehavior.STATE_EXPANDED
                    }
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState)
        bottomSheetDialog.setOnShowListener {
            (it as BottomSheetDialog).findViewById<FrameLayout>(R.id.design_bottom_sheet)
                ?.also { frameLayout ->
                    BottomSheetBehavior.from<FrameLayout?>(frameLayout).state =
                        BottomSheetBehavior.STATE_EXPANDED
                    BottomSheetBehavior.from<FrameLayout?>(frameLayout).skipCollapsed = true
                    BottomSheetBehavior.from<FrameLayout?>(frameLayout).isHideable = true
                }

        }
        return bottomSheetDialog
    }

}
