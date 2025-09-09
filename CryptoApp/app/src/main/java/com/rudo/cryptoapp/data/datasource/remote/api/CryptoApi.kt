package com.rudo.cryptoapp.data.datasource.remote.api

import com.rudo.cryptoapp.data.datasource.remote.dto.ApiResponseDto
import retrofit2.http.GET
import retrofit2.http.Header

interface CryptoApi {
    @GET("/v1/cryptocurrency/listings/latest")
    suspend fun getCryptoList(
        @Header("X-CMC_PRO_API_KEY") apiKey: String
    ): ApiResponseDto
}
