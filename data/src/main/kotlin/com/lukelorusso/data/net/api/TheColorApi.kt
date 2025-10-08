package com.lukelorusso.data.net.api

import com.lukelorusso.data.net.dto.TheColorResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TheColorApi {

    @GET("id")
    suspend fun getColor(
        @Query("hex") colorHex: String
    ): Response<TheColorResponseDTO>

}
