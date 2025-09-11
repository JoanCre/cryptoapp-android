package com.rudo.cryptoapp.presentation.screens.swap

import com.rudo.cryptoapp.domain.entities.ConversionResult
import com.rudo.cryptoapp.domain.entities.Cryptocurrency
import java.math.BigDecimal

data class SwapUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val cryptocurrencies: List<Cryptocurrency> = emptyList(),
    val fromCrypto: Cryptocurrency? = null,
    val toCrypto: Cryptocurrency? = null,
    val fromAmount: BigDecimal = BigDecimal.ZERO,
    val toAmount: BigDecimal = BigDecimal.ZERO,
    val conversionRate: BigDecimal = BigDecimal.ZERO,
    val lastConversion: ConversionResult? = null,
    val showCryptoSelector: Boolean = false,
    val selectorType: CryptoSelectorType = CryptoSelectorType.FROM,
    val searchQuery: String = "",
    val filteredCryptocurrencies: List<Cryptocurrency> = emptyList()
)

enum class CryptoSelectorType {
    FROM, TO
}

sealed class SwapIntent {
    data object LoadCryptocurrencies : SwapIntent()
    data object RefreshCryptocurrencies : SwapIntent()
    data class SelectFromCrypto(val crypto: Cryptocurrency) : SwapIntent()
    data class SelectToCrypto(val crypto: Cryptocurrency) : SwapIntent()
    data class UpdateFromAmount(val amount: String) : SwapIntent()
    data object SwapCryptocurrencies : SwapIntent()
    data object ShowFromCryptoSelector : SwapIntent()
    data object ShowToCryptoSelector : SwapIntent()
    data object HideCryptoSelector : SwapIntent()
    data class UpdateSearchQuery(val query: String) : SwapIntent()
    data object DismissError : SwapIntent()
}

sealed class SwapEffect { }
