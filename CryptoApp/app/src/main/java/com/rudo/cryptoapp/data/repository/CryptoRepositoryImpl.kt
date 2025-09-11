package com.rudo.cryptoapp.data.repository

import com.rudo.cryptoapp.data.datasource.CryptoDataSource
import com.rudo.cryptoapp.data.datasource.remote.dto.CryptocurrencyDto
import com.rudo.cryptoapp.domain.entities.Cryptocurrency
import com.rudo.cryptoapp.domain.repository.CryptoRepository
import java.math.BigDecimal
import javax.inject.Inject

class CryptoRepositoryImpl @Inject constructor(
    private val remoteDataSource: CryptoDataSource
) : CryptoRepository {

    override suspend fun getCryptocurrencies(): List<Cryptocurrency> {
        return try {
            val response = remoteDataSource.getCryptocurrencies()
            response.data.map { it.toDomain() }
        } catch (e: Exception) {
            throw e
        }
    }

    private fun CryptocurrencyDto.toDomain(): Cryptocurrency {
        val usdQuote = quote["USD"]
        val priceValue = usdQuote?.price ?: 0.0
        return Cryptocurrency(
            id = id,
            name = name,
            symbol = symbol,
            price = BigDecimal(priceValue.toString()),
            percentChange24h = usdQuote?.percentChange24h
        )
    }
}

