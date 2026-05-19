package com.lukelorusso.colorblindclick.data.di

import com.lukelorusso.colorblindclick.data.datasource.DatabaseDataSource
import com.lukelorusso.colorblindclick.data.datasource.HttpManager
import com.lukelorusso.colorblindclick.data.datasource.NetworkChecker
import com.lukelorusso.colorblindclick.data.datasource.PreferencesDataSource
import com.lukelorusso.colorblindclick.data.datasource.impl.DataStoreDataSourceImpl
import com.lukelorusso.colorblindclick.data.datasource.impl.RoomDbDataSourceImpl
import com.lukelorusso.colorblindclick.data.manager.DatabaseManager
import com.lukelorusso.colorblindclick.data.manager.PreferencesManager
import com.lukelorusso.colorblindclick.data.net.OkHttpClientFactory
import com.lukelorusso.colorblindclick.data.net.RetrofitFactory
import org.koin.dsl.module

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
val dataModule = module {
    //region Net
    factory { NetworkChecker(get()) }
    factory { HttpManager(get()) }
    factory { OkHttpClientFactory() }
    factory {
        RetrofitFactory.getRetrofitBuilder(
            get(),
            get(),
            get()
        )
    }
    //endregion

    //region Settings
    factory<PreferencesDataSource> { DataStoreDataSourceImpl() }
    factory { PreferencesManager(get()) }
    //endregion

    //region Persistence
    factory<DatabaseDataSource> { RoomDbDataSourceImpl() }
    factory { DatabaseManager(get()) }
    //endregion
} + dataCommonModule
