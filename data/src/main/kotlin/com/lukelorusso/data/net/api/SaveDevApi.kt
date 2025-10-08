package com.lukelorusso.data.net.api

import com.lukelorusso.data.net.SaveDevRetrofitFactory
import com.lukelorusso.data.net.dto.SaveDevResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SaveDevApi {

    @GET("colorblindness.php")
    suspend fun getColor(
        @Query("color_hex") colorHex: String,
        @Query("lang") language: String,
        @Query("udid") udid: String,
        @Query("os") os: String = SaveDevRetrofitFactory.API_OS,
        @Query("v") version: String = SaveDevRetrofitFactory.API_VERSION
    ): Response<SaveDevResponseDTO>

}
