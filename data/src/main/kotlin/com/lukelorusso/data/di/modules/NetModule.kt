package com.lukelorusso.data.di.modules

import android.content.Context
import com.google.gson.Gson
import com.lukelorusso.data.di.providers.NetworkChecker
import com.lukelorusso.data.net.OkHttpClientFactory
import com.lukelorusso.data.net.RetrofitFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Copyright (C) 2020 Mikhael LOPEZ
 * Licensed under the Apache License Version 2.0
 * Dagger module that provides Net class.
 */
@Module
class NetModule {

    @Provides
    @Singleton
    internal fun provideGson() = Gson()

    @Provides
    @Singleton
    internal fun provideNetworkChecker(context: Context) = NetworkChecker(context)

    @Provides
    @Singleton
    internal fun provideOkHttpClientFactory() = OkHttpClientFactory()

    @Provides
    @Singleton
    internal fun provideRetrofit(
        context: Context,
        gson: Gson,
        okHttpClientFactory: OkHttpClientFactory
    ) =
        RetrofitFactory.getRetrofitBuilder(context, gson, okHttpClientFactory)

}
