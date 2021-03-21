package com.lukelorusso.presentation_old.di.components

import android.app.Activity
import com.lukelorusso.presentation_old.di.PerActivity
import com.lukelorusso.presentation_old.di.modules.ActivityModule
import com.lukelorusso.presentation_old.scenes.camera.CameraFragment
import com.lukelorusso.presentation_old.scenes.history.HistoryFragment
import com.lukelorusso.presentation_old.scenes.info.InfoFragment
import com.lukelorusso.presentation_old.scenes.main.MainActivity
import com.lukelorusso.presentation_old.scenes.preview.PreviewDialogFragment
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
    fun inject(activity: MainActivity)

    fun inject(fragment: InfoFragment)

    fun inject(fragment: CameraFragment)

    fun inject(fragment: HistoryFragment)

    fun inject(dialogFragment: PreviewDialogFragment)
    //endregion

}
