package com.lukelorusso.presentation_old.di.components

import android.app.Application
import com.lukelorusso.data.di.components.DataComponent
import com.lukelorusso.presentation_old.di.PerApplication
import com.lukelorusso.presentation_old.di.modules.ApplicationModule
import com.lukelorusso.presentation_old.di.modules.UseCaseModule
import dagger.BindsInstance
import dagger.Component

/**
 * Copyright (C) 2020 Mikhael LOPEZ
 * Licensed under the Apache License Version 2.0
 * A component whose lifetime is the life of the application.
 */
@PerApplication // Constraints this component to one-per-application or un-scoped bindings.
@Component(
    dependencies = [(DataComponent::class)],
    modules = [(ApplicationModule::class), (UseCaseModule::class)]
)
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application,
            dataComponent: DataComponent
        ): ApplicationComponent
    }

    fun activityComponent(): ActivityComponent.Factory

}
