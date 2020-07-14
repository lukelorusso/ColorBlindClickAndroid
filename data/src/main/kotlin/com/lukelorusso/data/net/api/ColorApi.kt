package com.lukelorusso.data.net.api

import com.lukelorusso.data.net.RetrofitFactory
import com.lukelorusso.data.net.dto.ColorResponseDTO
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ColorApi {

    @GET("colorblindness.php")
    fun getColor(
        @Query("color_hex") colorHex: String,
        @Query("lang") language: String,
        @Query("udid") udid: String,
        @Query("os") os: String = RetrofitFactory.COLOR_API_OS,
        @Query("v") version: String = RetrofitFactory.COLOR_API_VERSION
    ): Single<ColorResponseDTO>

}
