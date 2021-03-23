package com.lukelorusso.presentation.scenes.base.view

import androidx.fragment.app.Fragment
import com.lukelorusso.presentation.AndroidApplication
import com.lukelorusso.presentation.di.components.ActivityComponent
import com.lukelorusso.presentation.di.components.ApplicationComponent

abstract class ABaseFragment : Fragment() {

    protected val appComponent: ApplicationComponent by lazy {
        (requireActivity().application as AndroidApplication).appComponent
    }

    protected val activityComponent: ActivityComponent by lazy {
        (activity as ABaseActivity).activityComponent
    }

}
