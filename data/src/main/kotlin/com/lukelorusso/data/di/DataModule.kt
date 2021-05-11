package com.lukelorusso.data.di

import com.google.gson.Gson
import com.lukelorusso.data.di.providers.*
import com.lukelorusso.data.extensions.api
import com.lukelorusso.data.mapper.ColorMapper
import com.lukelorusso.data.net.HttpServiceManager
import com.lukelorusso.data.net.OkHttpClientFactory
import com.lukelorusso.data.net.RetrofitFactory
import com.lukelorusso.data.repository.ColorDataRepository
import com.lukelorusso.domain.repository.ColorRepository
import org.koin.dsl.module
import retrofit2.Retrofit

val dataModule = module {
    //region Net
    factory { NetworkChecker(get()) }
    factory { HttpServiceManager(get()) }
    factory { OkHttpClientFactory() }
    factory { RetrofitFactory.getRetrofitBuilder(get(), get(), get()) }
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
    factory<SharedPrefProvider> { SharedPrefProvider.Impl(get()) }
    factory { SessionManager(get()) }
    //endregion

    //region Database
    factory<PersistenceProvider> { PersistenceProvider.Impl(get()) }
    factory { PersistenceManager(get()) }
    //endregion
}
