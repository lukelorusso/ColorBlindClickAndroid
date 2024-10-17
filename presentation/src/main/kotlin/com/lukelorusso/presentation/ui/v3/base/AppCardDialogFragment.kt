package com.lukelorusso.presentation.ui.v3.base

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.*
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.extensions.addOneTimeOnGlobalLayoutListener


/**
 * Copyright (C) 2024 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 *
 * Base [BottomSheetDialogFragment] class for every bottom dialog in this application.
 */
abstract class AppCardDialogFragment(
    private val isFullScreen: Boolean = false,
    private val isLocked: Boolean = isFullScreen
) : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.BottomSheetDialog

    protected val behavior by lazy {
        (dialog as BottomSheetDialog).behavior
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isLocked) {
            view.addOneTimeOnGlobalLayoutListener {
                if (isFullScreen) dialog?.findViewById<View>(R.id.design_bottom_sheet)?.apply {
                    layoutParams = layoutParams.apply {
                        height = ViewGroup.LayoutParams.MATCH_PARENT
                    }
                }

                with(behavior) {
                    if (isFullScreen) isFitToContents = false
                    state = BottomSheetBehavior.STATE_EXPANDED
                    addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                        override fun onStateChanged(bottomSheet: View, newState: Int) {
                            if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                            }
                        }

                        override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        }

                    })
                }
            }
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState)
        bottomSheetDialog.setOnShowListener { dialogInterface ->
            (dialogInterface as BottomSheetDialog).findViewById<FrameLayout>(R.id.design_bottom_sheet)
                ?.let { frameLayout -> BottomSheetBehavior.from<FrameLayout>(frameLayout) }
                ?.apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    skipCollapsed = true
                    isHideable = true
                }

        }
        return bottomSheetDialog
    }

}
