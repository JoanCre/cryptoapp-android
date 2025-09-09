package com.rudo.cryptoapp.domain.usecase

import com.rudo.cryptoapp.domain.entities.ConversionResult
import com.rudo.cryptoapp.domain.entities.Cryptocurrency
import java.math.BigDecimal

interface CryptoUseCase {
    suspend fun getCryptocurrencies(): List<Cryptocurrency>
    suspend fun refreshCryptocurrencies(): List<Cryptocurrency>
    fun convertCrypto(
        fromCrypto: Cryptocurrency,
        toCrypto: Cryptocurrency,
        amount: BigDecimal
    ): ConversionResult

    fun calculateConversionRate(
        fromCrypto: Cryptocurrency,
        toCrypto: Cryptocurrency
    ): BigDecimal
}
