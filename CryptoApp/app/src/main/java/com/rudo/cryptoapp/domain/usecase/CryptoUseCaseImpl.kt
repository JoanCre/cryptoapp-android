package com.rudo.cryptoapp.domain.usecase

import com.rudo.cryptoapp.domain.entities.ConversionResult
import com.rudo.cryptoapp.domain.entities.Cryptocurrency
import com.rudo.cryptoapp.domain.repository.CryptoRepository
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class CryptoUseCaseImpl @Inject constructor(
    private val repository: CryptoRepository
) : CryptoUseCase {

    private var cachedCryptocurrencies: List<Cryptocurrency> = emptyList()
    private var lastUpdateTime: Long = 0

    companion object {
        private const val CACHE_DURATION_MS = 60_000L // 1 minute cache
    }

    override suspend fun getCryptocurrencies(): List<Cryptocurrency> {
        val currentTime = System.currentTimeMillis()

        return if (cachedCryptocurrencies.isEmpty() ||
            (currentTime - lastUpdateTime) > CACHE_DURATION_MS
        ) {
            refreshCryptocurrencies()
        } else {
            cachedCryptocurrencies
        }
    }

    override suspend fun refreshCryptocurrencies(): List<Cryptocurrency> {
        cachedCryptocurrencies = repository.getCryptocurrencies()
        lastUpdateTime = System.currentTimeMillis()
        return cachedCryptocurrencies
    }

    override fun convertCrypto(
        fromCrypto: Cryptocurrency,
        toCrypto: Cryptocurrency,
        amount: BigDecimal
    ): ConversionResult {
        val conversionRate = calculateConversionRate(fromCrypto, toCrypto)
        val convertedAmount = amount.multiply(conversionRate)
            .setScale(8, RoundingMode.HALF_UP) // 8 decimal

        return ConversionResult(
            fromCrypto = fromCrypto,
            toCrypto = toCrypto,
            fromAmount = amount,
            toAmount = convertedAmount,
            conversionRate = conversionRate
        )
    }

    override fun calculateConversionRate(
        fromCrypto: Cryptocurrency,
        toCrypto: Cryptocurrency
    ): BigDecimal {
        // If same crypto rate is 1
        if (fromCrypto.id == toCrypto.id) {
            return BigDecimal.ONE
        }

        // If either price is zero return zero to avoid division by zero
        if (fromCrypto.price == BigDecimal.ZERO || toCrypto.price == BigDecimal.ZERO) {
            return BigDecimal.ZERO
        }

        // Conversion rate = fromPrice / toPrice
        // Example: BTC ($50,000) to ETH ($3,000) = 50,000 / 3,000 = 16.67 ETH per BTC
        return fromCrypto.price.divide(toCrypto.price, 8, RoundingMode.HALF_UP)
    }
}
