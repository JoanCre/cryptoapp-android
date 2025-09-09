package com.rudo.cryptoapp.data.datasource

import com.rudo.cryptoapp.data.datasource.remote.dto.ApiResponseDto

interface CryptoDataSource {
    suspend fun getCryptocurrencies(): ApiResponseDto
}
