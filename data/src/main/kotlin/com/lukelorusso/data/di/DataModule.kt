package com.lukelorusso.data.di

import com.lukelorusso.data.datasource.*
import com.lukelorusso.data.datasource.impl.PersistenceDataSourceImpl
import com.lukelorusso.data.datasource.impl.SharedPrefDataSourceImpl
import com.lukelorusso.data.extensions.api
import com.lukelorusso.data.mapper.ColorMapper
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
 */
val dataModule = module {
    //region Net
    factory { NetworkChecker(get()) }
    factory { HttpManager(get()) }
    factory { OkHttpClientFactory() }
    factory { RetrofitFactory.getRetrofitBuilder(get(), get(), get()) }
    //endregion

    //region Mapper
    factory<Json> { Json { ignoreUnknownKeys = true } }
    factory { ColorMapper() }
    //endregion

    //region Repository
    factory<ColorRepository> {
        ColorRepositoryImpl(
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
    factory<SharedPrefDataSource> { SharedPrefDataSourceImpl(get()) }
    factory { SettingsManager(get()) }
    //endregion

    //region Persistence
    factory<PersistenceDataSource> { PersistenceDataSourceImpl(get()) }
    //endregion
}
