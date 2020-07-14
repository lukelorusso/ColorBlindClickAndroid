package com.lukelorusso.data.net

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * Copyright (C) 2020 Mikhael LOPEZ
 * Licensed under the Apache License Version 2.0
 * RetrofitFactory to generate a Retrofit instance.
 * It sets up request logging and a Gson type adapter.
 */
object RetrofitFactory {

    private const val COLOR_API_BASE_URL =
        "https://savedev.altervista.org/SD-Frontend/colorblindness/"
    private const val COLOR_BLIND_SITE =
        "http://www.savedev.altervista.org/" //"http://www.colorblindclick.com/"
    const val COLOR_API_OS = "android"
    const val COLOR_API_VERSION = "1"
    const val COLOR_BLIND_SITE_HELP =
        COLOR_BLIND_SITE + "SD-Frontend/colorblindclick/help.php?lang=%s"
    const val COLOR_BLIND_SITE_HOME =
        COLOR_BLIND_SITE + "SD-Frontend/colorblindclick/index.php?setlang=%s"
    const val COLOR_BLIND_SITE_ABOUT_ME = "http://www.lukelorusso.com/"

    /**
     * Get [Retrofit] instance for main webservice.
     * @return instances of [Retrofit]
     */
    @RequiresPermission(value = Manifest.permission.INTERNET)
    fun getLocalRetrofit(
        context: Context,
        gson: Gson,
        okHttpClientFactory: OkHttpClientFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(COLOR_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClientFactory.createOkHttpClient(context))
            .build()

}
