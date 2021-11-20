package com.lukelorusso.data.net

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.lukelorusso.data.BuildConfig
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
open class OkHttpClientFactory {

    open fun createOkHttpClient(context: Context? = null): OkHttpClient =
        OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                    enableDebugTools(context)
                }
                updateTimeout()
            }
            .build()


    private fun OkHttpClient.Builder.enableDebugTools(context: Context?) {
        addInterceptor(StethoInterceptor())
        context?.also {
            addInterceptor(
                ChuckerInterceptor.Builder(it)
                    .collector(
                        ChuckerCollector(
                            context = it,
                            retentionPeriod = RetentionManager.Period.ONE_DAY
                        )
                    )
                    .build()
            )
        }
    }

    private fun OkHttpClient.Builder.updateTimeout(read: Long = 60, write: Long = 60) {
        readTimeout(read, TimeUnit.SECONDS)
        writeTimeout(write, TimeUnit.SECONDS)
    }

}
