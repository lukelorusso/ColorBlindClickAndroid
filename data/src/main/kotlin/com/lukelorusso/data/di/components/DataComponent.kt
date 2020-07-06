package com.lukelorusso.data.di.components

import android.content.Context
import com.google.gson.Gson
import com.lukelorusso.data.di.modules.NetModule
import com.lukelorusso.data.di.modules.PersistenceModule
import com.lukelorusso.data.di.modules.RepositoryModule
import com.lukelorusso.data.di.modules.SessionModule
import com.lukelorusso.domain.repository.ColorRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * Copyright (C) 2020 Mikhael LOPEZ
 * Licensed under the Apache License Version 2.0
 */
@Singleton
@Component(
    modules = [(NetModule::class),
        (PersistenceModule::class),
        (RepositoryModule::class),
        (SessionModule::class)]
)
interface DataComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): DataComponent
    }

    // Exposed to sub-graphs
    fun provideGson(): Gson

    fun provideColorRepository(): ColorRepository

}
