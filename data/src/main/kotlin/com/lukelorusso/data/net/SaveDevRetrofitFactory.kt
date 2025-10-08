package com.lukelorusso.data.net

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object SaveDevRetrofitFactory {

    private const val API_BASE_URL =
        "https://savedev.altervista.org/SD-Frontend/colorblindness/"
    private const val WEBSITE =
        "https://savedev.altervista.org/"
    const val API_OS = "android"
    const val API_VERSION = "1"
    const val WEBSITE_HELP =
        WEBSITE + "SD-Frontend/colorblindclick/help.php?lang=%s"
    const val WEBSITE_HOME =
        WEBSITE + "SD-Frontend/colorblindclick/index.php?setlang=%s"

    /**
     * Get [Retrofit] instance for main webservice.
     * @return instances of [Retrofit]
     */
    @RequiresPermission(value = Manifest.permission.INTERNET)
    fun getRetrofitBuilder(
        okHttpClientFactory: OkHttpClientFactory,
        json: Json,
        context: Context? = null
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .client(okHttpClientFactory.createOkHttpClient(context))
            .build()

}
