package com.lukelorusso.presentation.scenes.base.view

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lukelorusso.presentation.AndroidApplication
import com.lukelorusso.presentation.R
import com.lukelorusso.presentation.di.components.ActivityComponent
import com.lukelorusso.presentation.di.components.ApplicationComponent
import com.lukelorusso.presentation.scenes.base.viewmodel.AViewModel
import javax.inject.Inject


/**
 * Copyright (C) 2020 Luke Lorusso, based on ABaseDataFragment
 * Licensed under the Apache License Version 2.0
 * Base [BottomSheetDialogFragment] class for every fragment in this application.
 */
abstract class ABaseBottomSheetDialogFragment<ViewModel : AViewModel<Data>, Data : Any>(
        private val viewModelType: Class<ViewModel>,
        private val isFullScreen: Boolean = false
) : BottomSheetDialogFragment(), ADataView<Data> {

    class ABaseBottomSheetDialogFragmentViewBindingInjector {
        @Inject
        lateinit var viewModelFactory: ViewModelProvider.Factory
    }

    private val injector = ABaseBottomSheetDialogFragmentViewBindingInjector()
    lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent.inject(injector)
        viewModel = ViewModelProvider(this, injector.viewModelFactory).get(viewModelType)
        viewModel.initRouter(requireActivity() as AppCompatActivity, this)
        super.onCreate(savedInstanceState)
    }

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
