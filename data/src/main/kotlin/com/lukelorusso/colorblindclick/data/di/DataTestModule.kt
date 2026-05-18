package com.lukelorusso.colorblindclick.data.di

import com.lukelorusso.colorblindclick.data.datasource.DatabaseDataSource
import com.lukelorusso.colorblindclick.data.datasource.HttpManager
import com.lukelorusso.colorblindclick.data.datasource.PreferencesDataSource
import com.lukelorusso.colorblindclick.data.datasource.PreferencesManager
import com.lukelorusso.colorblindclick.data.datasource.impl.DatabaseDataSourceTestImpl
import com.lukelorusso.colorblindclick.data.datasource.impl.MockedDataSourceImpl
import com.lukelorusso.colorblindclick.data.net.OkHttpClientFactory
import com.lukelorusso.colorblindclick.data.net.RetrofitFactory
import org.koin.dsl.module

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 *
 * A dataModule variant, lacking of any instance of [android.content.Context].
 */
val dataTestModule = module {
    //region Net
    factory { HttpManager() }
    factory { OkHttpClientFactory() }
    factory {
        RetrofitFactory.getRetrofitBuilder(
            get(),
            get()
        )
    }
    //endregion

    //region Settings
    factory<PreferencesDataSource> { MockedDataSourceImpl(get()) }
    factory { PreferencesManager(get()) }
    //endregion

    //region Persistence
    factory<DatabaseDataSource> { DatabaseDataSourceTestImpl(get()) }
    //endregion
} + dataCommonModule
