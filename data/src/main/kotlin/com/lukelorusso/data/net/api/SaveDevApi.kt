package com.lukelorusso.data.net.api

import com.lukelorusso.data.net.RetrofitFactory
import com.lukelorusso.data.net.dto.SaveDevResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SaveDevApi {

    @GET("${RetrofitFactory.SaveDevApi.API_BASE_URL}colorblindness.php")
    suspend fun getColor(
        @Query("color_hex") colorHex: String,
        @Query("lang") language: String,
        @Query("udid") udid: String,
        @Query("os") os: String = RetrofitFactory.SaveDevApi.API_OS,
        @Query("v") version: String = RetrofitFactory.SaveDevApi.API_VERSION
    ): Response<SaveDevResponseDTO>

}
