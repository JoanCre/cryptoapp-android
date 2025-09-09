package com.rudo.cryptoapp.domain.entities

import java.math.BigDecimal

data class Cryptocurrency(
    val id: Int,
    val name: String,
    val symbol: String,
    val price: BigDecimal,
    val percentChange24h: Double?
)

data class ConversionResult(
    val fromCrypto: Cryptocurrency,
    val toCrypto: Cryptocurrency,
    val fromAmount: BigDecimal,
    val toAmount: BigDecimal,
    val conversionRate: BigDecimal,
    val timestamp: Long = System.currentTimeMillis()
)
