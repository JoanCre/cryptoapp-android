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

    /**
     * Calculates the conversion rate between two cryptocurrencies.
     *
     * The conversion rate represents how much of the target cryptocurrency (toCrypto)
     * you can get for 1 unit of the source cryptocurrency (fromCrypto).
     *
     * Formula: rate = fromPrice / toPrice
     *
     * Example:
     * - XRP price: $3.03
     * - BTC price: $113,944
     * - Rate: 3.03 / 113,944 = 0.0000266 BTC per XRP
     *
     * This means 1 XRP = 0.0000266 BTC
     *
     * @param fromCrypto The source cryptocurrency to convert from
     * @param toCrypto The target cryptocurrency to convert to
     * @return The conversion rate with 8 decimal places precision, or ZERO if calculation fails
     */
    override fun calculateConversionRate(
        fromCrypto: Cryptocurrency,
        toCrypto: Cryptocurrency
    ): BigDecimal {
        return try {
            // Same cryptocurrency always has 1:1 rate
            if (fromCrypto.id == toCrypto.id) {
                return BigDecimal.ONE
            }

            // Validate that prices are valid for calculation
            if (!isPriceValid(fromCrypto.price) || !isPriceValid(toCrypto.price)) {
                return BigDecimal.ZERO
            }

            // Calculate conversion rate: fromPrice / toPrice
            fromCrypto.price.divide(toCrypto.price, 8, RoundingMode.HALF_UP)
        } catch (e: ArithmeticException) {
            // Handle any division errors gracefully
            BigDecimal.ZERO
        }
    }

    /**
     * Validates if a price is valid for conversion calculations.
     *
     * @param price The price to validate
     * @return true if price is valid (positive and not null), false otherwise
     */
    private fun isPriceValid(price: BigDecimal?): Boolean {
        return price != null &&
            price.compareTo(BigDecimal.ZERO) > 0 &&
            price.compareTo(BigDecimal.valueOf(Double.MAX_VALUE)) <= 0
    }
}
