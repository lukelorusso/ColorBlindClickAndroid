package com.lukelorusso.data.di

import com.lukelorusso.data.datasource.*
import com.lukelorusso.data.datasource.impl.DatabaseDataSourceTestImpl
import com.lukelorusso.data.datasource.impl.SharedPrefDataSourceTestImpl
import com.lukelorusso.data.extensions.api
import com.lukelorusso.data.mapper.SaveDevMapper
import com.lukelorusso.data.mapper.TheColorMapper
import com.lukelorusso.data.net.OkHttpClientFactory
import com.lukelorusso.data.net.RetrofitFactory
import com.lukelorusso.data.repository.*
import com.lukelorusso.domain.repository.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import retrofit2.Retrofit

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
    factory { RetrofitFactory.getRetrofitBuilder(get(), get()) }
    //endregion

    //region Mapper
    factory<Json> { Json { ignoreUnknownKeys = true } }
    factory { SaveDevMapper() }
    factory { TheColorMapper() }
    //endregion

    //region Repository
    factory<SaveDevApiRepository> {
        SaveDevApiRepositoryImpl(
            (get() as Retrofit).api(),
            get(),
            get(),
            get()
        )
    }
    factory<TheColorApiRepository> {
        TheColorApiRepositoryImpl(
            (get() as Retrofit).api(),
            get(),
            get(),
            get()
        )
    }
    factory<InfoRepository> { InfoRepositoryImpl() }
    factory<HistoryRepository> { HistoryRepositoryImpl(get()) }
    factory<SettingsRepository> { SettingsRepositoryImpl(get()) }
    //endregion

    //region Settings
    factory<SharedPrefDataSource> { SharedPrefDataSourceTestImpl(get()) }
    factory { PersistenceManager(get()) }
    //endregion

    //region Persistence
    factory<DatabaseDataSource> { DatabaseDataSourceTestImpl(get()) }
    //endregion
}
