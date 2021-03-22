package com.lukelorusso.presentation.di.components

import android.app.Activity
import com.lukelorusso.presentation.di.PerActivity
import com.lukelorusso.presentation.di.modules.ActivityModule
import com.lukelorusso.presentation.scenes.base.view.ABaseBottomSheetDialogFragment
import com.lukelorusso.presentation.scenes.base.view.ABaseDataFragment
import com.lukelorusso.presentation.scenes.camera.CameraFragment
import com.lukelorusso.presentation.scenes.history.HistoryFragment
import com.lukelorusso.presentation.scenes.info.InfoFragment
import com.lukelorusso.presentation.scenes.main.MainActivity
import com.lukelorusso.presentation.scenes.preview.PreviewDialogFragment
import dagger.BindsInstance
import dagger.Subcomponent

@PerActivity
@Subcomponent(modules = [(ActivityModule::class)])
interface ActivityComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: Activity): ActivityComponent
    }

    //region Inject
    fun inject(injector: ABaseDataFragment.ABaseDataFragmentViewBindingInjector)

    fun inject(injector: ABaseBottomSheetDialogFragment.ABaseBottomSheetDialogFragmentViewBindingInjector)

    fun inject(activity: MainActivity)

    fun inject(fragment: InfoFragment)

    fun inject(fragment: CameraFragment)

    fun inject(fragment: HistoryFragment)

    fun inject(dialogFragment: PreviewDialogFragment)
    //endregion

}
