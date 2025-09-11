package com.rudo.cryptoapp.presentation.screens.swap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudo.cryptoapp.domain.entities.Cryptocurrency
import com.rudo.cryptoapp.domain.usecase.CryptoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class SwapViewModel @Inject constructor(
    private val cryptoUseCase: CryptoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SwapUiState())
    val uiState: StateFlow<SwapUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<SwapEffect>()
    val effect: Flow<SwapEffect> = _effect

    private var refreshJob: Job? = null
    private var autoRefreshJob: Job? = null

    init {
        startAutoRefresh()
    }

    fun handleIntent(intent: SwapIntent) {
        when (intent) {
            is SwapIntent.LoadCryptocurrencies -> loadCryptocurrencies()
            is SwapIntent.RefreshCryptocurrencies -> refreshCryptocurrencies()
            is SwapIntent.SelectFromCrypto -> selectFromCrypto(intent.crypto)
            is SwapIntent.SelectToCrypto -> selectToCrypto(intent.crypto)
            is SwapIntent.UpdateFromAmount -> updateFromAmount(intent.amount)
            is SwapIntent.SwapCryptocurrencies -> swapCryptocurrencies()
            is SwapIntent.ShowFromCryptoSelector -> showFromCryptoSelector()
            is SwapIntent.ShowToCryptoSelector -> showToCryptoSelector()
            is SwapIntent.HideCryptoSelector -> hideCryptoSelector()
            is SwapIntent.UpdateSearchQuery -> updateSearchQuery(intent.query)
            is SwapIntent.DismissError -> dismissError()
        }
    }

    private fun loadCryptocurrencies() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }
                val cryptocurrencies = cryptoUseCase.getCryptocurrencies()

                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        cryptocurrencies = cryptocurrencies,
                        filteredCryptocurrencies = filterCryptocurrencies(
                            cryptocurrencies,
                            currentState.searchQuery
                        ),
                        fromCrypto = currentState.fromCrypto?.let { current ->
                            cryptocurrencies.find { it.id == current.id }
                                ?: cryptocurrencies.firstOrNull()
                        } ?: cryptocurrencies.firstOrNull(),
                        toCrypto = currentState.toCrypto?.let { current ->
                            cryptocurrencies.find { it.id == current.id }
                                ?: cryptocurrencies.getOrNull(1)
                        } ?: cryptocurrencies.getOrNull(1)
                    )
                }

                // Calculate initial conversion if both cryptos are selected
                val state = _uiState.value
                if (state.fromCrypto != null && state.toCrypto != null) {
                    calculateConversion()
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error loading cryptocurrencies"
                    )
                }
            }
        }
    }

    private fun refreshCryptocurrencies() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            try {
                _uiState.update { it.copy(isRefreshing = true, error = null) }
                val cryptocurrencies = cryptoUseCase.refreshCryptocurrencies()

                _uiState.update { currentState ->
                    val updatedFromCrypto = currentState.fromCrypto?.let { current ->
                        cryptocurrencies.find { it.id == current.id }
                            ?: cryptocurrencies.firstOrNull()
                    } ?: cryptocurrencies.firstOrNull()

                    val updatedToCrypto = currentState.toCrypto?.let { current ->
                        cryptocurrencies.find { it.id == current.id } ?: cryptocurrencies.getOrNull(
                            1
                        )
                    } ?: cryptocurrencies.getOrNull(1)

                    currentState.copy(
                        isRefreshing = false,
                        cryptocurrencies = cryptocurrencies,
                        filteredCryptocurrencies = filterCryptocurrencies(
                            cryptocurrencies,
                            currentState.searchQuery
                        ),
                        fromCrypto = updatedFromCrypto,
                        toCrypto = updatedToCrypto
                    )
                }

                // Recalculate conversion with updated prices
                calculateConversion()

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isRefreshing = false,
                        error = "Error refreshing cryptocurrencies"
                    )
                }
            }
        }
    }

    private fun selectFromCrypto(crypto: Cryptocurrency) {
        _uiState.update {
            it.copy(
                fromCrypto = crypto,
                showCryptoSelector = false
            )
        }
        calculateConversion()
    }

    private fun selectToCrypto(crypto: Cryptocurrency) {
        _uiState.update {
            it.copy(
                toCrypto = crypto,
                showCryptoSelector = false
            )
        }
        calculateConversion()
    }

    private fun updateFromAmount(amount: String) {
        val bigDecimalAmount = try {
            if (amount.isEmpty()) BigDecimal.ZERO else BigDecimal(amount)
        } catch (e: NumberFormatException) {
            return // Invalid number format, ignore
        }

        _uiState.update { it.copy(fromAmount = bigDecimalAmount) }
        calculateConversion()
    }


    private fun swapCryptocurrencies() {
        val currentState = _uiState.value
        if (currentState.fromCrypto != null && currentState.toCrypto != null) {
            _uiState.update {
                it.copy(
                    fromCrypto = currentState.toCrypto,
                    toCrypto = currentState.fromCrypto,
                    fromAmount = currentState.toAmount,
                    toAmount = currentState.fromAmount
                )
            }
            calculateConversion()
        }
    }

    private fun showFromCryptoSelector() {
        _uiState.update {
            it.copy(
                showCryptoSelector = true,
                selectorType = CryptoSelectorType.FROM
            )
        }
    }

    private fun showToCryptoSelector() {
        _uiState.update {
            it.copy(
                showCryptoSelector = true,
                selectorType = CryptoSelectorType.TO
            )
        }
    }

    private fun hideCryptoSelector() {
        _uiState.update {
            it.copy(showCryptoSelector = false)
        }
    }

    private fun startAutoRefresh() {
        autoRefreshJob?.cancel()
        autoRefreshJob = viewModelScope.launch {
            // Initial load
            loadCryptocurrencies()

            // Auto-refresh every minute
            while (true) {
                delay(60_000) // 1 minute
                if (!_uiState.value.isLoading && !_uiState.value.isRefreshing) {
                    refreshCryptocurrencies()
                }
            }
        }
    }

    private fun calculateConversion() {
        val state = _uiState.value
        val fromCrypto = state.fromCrypto
        val toCrypto = state.toCrypto

        if (fromCrypto != null && toCrypto != null) {
            val conversionRate = cryptoUseCase.calculateConversionRate(fromCrypto, toCrypto)

            val result = if (state.fromAmount > BigDecimal.ZERO) {
                cryptoUseCase.convertCrypto(fromCrypto, toCrypto, state.fromAmount)
            } else {
                null
            }

            _uiState.update {
                it.copy(
                    toAmount = result?.toAmount ?: BigDecimal.ZERO,
                    conversionRate = conversionRate,
                    lastConversion = result
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    toAmount = BigDecimal.ZERO,
                    conversionRate = BigDecimal.ZERO,
                    lastConversion = null
                )
            }
        }
    }

    private fun updateSearchQuery(query: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = query,
                filteredCryptocurrencies = filterCryptocurrencies(
                    currentState.cryptocurrencies,
                    query
                )
            )
        }
    }

    private fun filterCryptocurrencies(
        cryptocurrencies: List<Cryptocurrency>,
        query: String
    ): List<Cryptocurrency> {
        return if (query.isBlank()) {
            cryptocurrencies
        } else {
            cryptocurrencies.filter { crypto ->
                crypto.name.contains(query, ignoreCase = true) ||
                    crypto.symbol.contains(query, ignoreCase = true)
            }
        }
    }

    private fun dismissError() {
        _uiState.update { it.copy(error = null) }
    }

    override fun onCleared() {
        super.onCleared()
        refreshJob?.cancel()
        autoRefreshJob?.cancel()
    }
}
