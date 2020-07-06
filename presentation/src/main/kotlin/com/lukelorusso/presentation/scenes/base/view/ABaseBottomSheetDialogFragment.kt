package com.lukelorusso.presentation.scenes.base.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.LayoutRes
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lukelorusso.presentation.AndroidApplication
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.di.components.ActivityComponent
import com.lukelorusso.presentation.di.components.ApplicationComponent

/**
 * Copyright (C) 2020 Luke Lorusso, based on ABaseDataFragment
 * Licensed under the Apache License Version 2.0
 * Base [BottomSheetDialogFragment] class for every fragment in this application.
 */
abstract class ABaseBottomSheetDialogFragment(
    @LayoutRes private val contentLayoutId: Int,
    private val isFullScreen: Boolean = false
) : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    protected val appComponent: ApplicationComponent by lazy {
        (requireActivity().application as AndroidApplication).appComponent
    }

    protected val activityComponent: ActivityComponent by lazy {
        (activity as ABaseActivity).activityComponent
    }

    protected val behavior by lazy {
        (dialog as BottomSheetDialog).behavior
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (contentLayoutId != 0) {
            inflater.inflate(contentLayoutId, container, false)
        } else null
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
                            com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
                    }
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }

    }

}
