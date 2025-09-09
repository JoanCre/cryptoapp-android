package com.rudo.cryptoapp.data.datasource.remote

import com.rudo.cryptoapp.data.datasource.CryptoDataSource
import com.rudo.cryptoapp.data.datasource.remote.api.CryptoApi
import com.rudo.cryptoapp.data.datasource.remote.dto.ApiResponseDto
import javax.inject.Inject

class CryptoRemoteDataSourceImpl @Inject constructor(
    private val cryptoApi: CryptoApi
) : CryptoDataSource {

    companion object {
        private const val API_KEY = "b09dca7f-85a8-4356-9f21-bfbe49bbd655"
    }

    override suspend fun getCryptocurrencies(): ApiResponseDto {
        return cryptoApi.getCryptoList(API_KEY)
    }
}
