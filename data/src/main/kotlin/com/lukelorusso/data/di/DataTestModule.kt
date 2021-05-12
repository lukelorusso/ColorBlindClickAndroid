package com.lukelorusso.data.di

import com.google.gson.Gson
import com.lukelorusso.data.di.providers.PersistenceManager
import com.lukelorusso.data.di.providers.PersistenceProvider
import com.lukelorusso.data.di.providers.SessionManager
import com.lukelorusso.data.di.providers.SharedPrefProvider
import com.lukelorusso.data.extensions.api
import com.lukelorusso.data.mapper.ColorMapper
import com.lukelorusso.data.net.HttpServiceManager
import com.lukelorusso.data.net.OkHttpClientFactory
import com.lukelorusso.data.net.RetrofitFactory
import com.lukelorusso.data.repository.ColorDataRepository
import com.lukelorusso.domain.repository.ColorRepository
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
    factory { HttpServiceManager() }
    factory { OkHttpClientFactory() }
    factory { RetrofitFactory.getRetrofitTestBuilder(get(), get()) }
    //endregion

    //region Mapper
    factory { Gson() }
    factory { ColorMapper() }
    //endregion

    //region Repository
    factory<ColorRepository> {
        ColorDataRepository(
            (get() as Retrofit).api(),
            get(),
            get(),
            get(),
            get()
        )
    }
    //endregion

    //region Session
    factory<SharedPrefProvider> { SharedPrefProvider.TestImpl(get()) }
    factory { SessionManager(get()) }
    //endregion

    //region Persistence
    factory<PersistenceProvider> { PersistenceProvider.TestImpl(get()) }
    factory { PersistenceManager(get()) }
    //endregion
}
