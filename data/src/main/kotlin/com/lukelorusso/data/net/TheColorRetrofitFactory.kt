package com.lukelorusso.data.net

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object TheColorRetrofitFactory {

    private const val COLOR_API_BASE_URL =
        "https://www.thecolorapi.com/"
    private const val COLOR_API_SITE =
        "https://www.thecolorapi.com/"

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
            .baseUrl(COLOR_API_BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .client(okHttpClientFactory.createOkHttpClient(context))
            .build()

}
