package com.lukelorusso.colorblindclick.data.di

import com.lukelorusso.colorblindclick.data.datasource.*
import com.lukelorusso.colorblindclick.data.datasource.impl.DatabaseDataSourceImpl
import com.lukelorusso.colorblindclick.data.datasource.impl.SharedPrefDataSourceImpl
import com.lukelorusso.colorblindclick.data.extensions.api
import com.lukelorusso.colorblindclick.data.mapper.SaveDevMapper
import com.lukelorusso.colorblindclick.data.mapper.TheColorMapper
import com.lukelorusso.colorblindclick.data.net.OkHttpClientFactory
import com.lukelorusso.colorblindclick.data.net.RetrofitFactory
import com.lukelorusso.colorblindclick.data.repository.*
import com.lukelorusso.domain.repository.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import retrofit2.Retrofit

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
    factory<SharedPrefDataSource> {
        SharedPrefDataSourceImpl(
            get()
        )
    }
    factory { PersistenceManager(get()) }
    //endregion

    //region Persistence
    factory<DatabaseDataSource> {
        DatabaseDataSourceImpl(
            get()
        )
    }
    //endregion
} + dataCommonModule
