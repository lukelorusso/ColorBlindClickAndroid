package com.lukelorusso.colorblindclick.data.net.api

import com.lukelorusso.colorblindclick.data.net.RetrofitFactory
import com.lukelorusso.colorblindclick.data.net.dto.TheColorResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TheColorApi {

    @GET("${RetrofitFactory.TheColorApi.API_BASE_URL}id")
    suspend fun getColor(
        @Query("hex") colorHex: String
    ): Response<TheColorResponseDTO>

}
