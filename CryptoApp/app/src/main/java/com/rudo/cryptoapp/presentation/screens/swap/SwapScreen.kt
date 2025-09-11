package com.rudo.cryptoapp.presentation.screens.swap

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rudo.cryptoapp.domain.entities.Cryptocurrency
import com.rudo.cryptoapp.presentation.components.ConversionRateText
import com.rudo.cryptoapp.presentation.components.CryptoIcon
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.rudo.cryptoapp.presentation.components.PriceDisplay
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwapScreen(
    viewModel: SwapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.handleIntent(SwapIntent.LoadCryptocurrencies)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Swap Cryptocurrencies",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.handleIntent(SwapIntent.RefreshCryptocurrencies) }
                    ) {
                        if (uiState.isRefreshing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
            }
        } else {
            SwapContent(
                modifier = Modifier.padding(paddingValues),
                uiState = uiState,
                viewModel = viewModel,
                onFromCryptoClick = {
                    viewModel.handleIntent(SwapIntent.ShowFromCryptoSelector)
                },
                onToCryptoClick = {
                    viewModel.handleIntent(SwapIntent.ShowToCryptoSelector)
                },
                onSwapClick = {
                    viewModel.handleIntent(SwapIntent.SwapCryptocurrencies)
                },
                onCryptoSelect = { crypto ->
                    when (uiState.selectorType) {
                        CryptoSelectorType.FROM -> viewModel.handleIntent(
                            SwapIntent.SelectFromCrypto(
                                crypto
                            )
                        )

                        CryptoSelectorType.TO -> viewModel.handleIntent(
                            SwapIntent.SelectToCrypto(
                                crypto
                            )
                        )
                    }
                    viewModel.handleIntent(SwapIntent.HideCryptoSelector)
                },
                onDismissSelector = {
                    viewModel.handleIntent(SwapIntent.HideCryptoSelector)
                }
            )
        }
    }

    // Error Dialog
    uiState.error?.let { errorMessage ->
        AlertDialog(
            onDismissRequest = {
                viewModel.handleIntent(SwapIntent.DismissError)
            },
            title = {
                Text("Error")
            },
            text = {
                Text(errorMessage)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.handleIntent(SwapIntent.DismissError)
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
private fun SwapContent(
    modifier: Modifier = Modifier,
    uiState: SwapUiState,
    viewModel: SwapViewModel,
    onFromCryptoClick: () -> Unit,
    onToCryptoClick: () -> Unit,
    onSwapClick: () -> Unit,
    onCryptoSelect: (Cryptocurrency) -> Unit,
    onDismissSelector: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // From Crypto Card
        CryptoCard(
            label = "From",
            crypto = uiState.fromCrypto,
            onCryptoClick = onFromCryptoClick
        )

        // Swap Button
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onSwapClick,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(50)
                    )
                    .size(48.dp)
            ) {
                Icon(
                    Icons.Default.SwapVert,
                    contentDescription = "Swap",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        CryptoCard(
            label = "To",
            crypto = uiState.toCrypto,
            onCryptoClick = onToCryptoClick
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Conversion Result Card
        ConversionResultCard(
            fromCrypto = uiState.fromCrypto,
            toCrypto = uiState.toCrypto,
            conversionRate = uiState.conversionRate
        )

        Spacer(modifier = Modifier.weight(1f))
    }

    // Crypto Selector Modal
    if (uiState.showCryptoSelector) {
        CryptoSelectorModal(
            filteredCryptocurrencies = uiState.filteredCryptocurrencies,
            searchQuery = uiState.searchQuery,
            onSearchQueryChange = { query ->
                viewModel.handleIntent(SwapIntent.UpdateSearchQuery(query))
            },
            onCryptoSelect = onCryptoSelect,
            onDismiss = onDismissSelector
        )
    }
}

@Composable
private fun CryptoCard(
    label: String,
    crypto: Cryptocurrency?,
    onCryptoClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    PriceDisplay(
                        price = crypto?.price,
                        percentChange = crypto?.percentChange24h
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.clickable { onCryptoClick() },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CryptoIcon(
                        symbol = crypto?.symbol ?: "",
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = crypto?.symbol ?: "Select",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = "Select crypto",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ConversionResultCard(
    fromCrypto: Cryptocurrency?,
    toCrypto: Cryptocurrency?,
    conversionRate: BigDecimal
) {
    if (fromCrypto != null && toCrypto != null) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Conversion Rate",
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                ConversionRateText(
                    fromSymbol = fromCrypto.symbol,
                    toSymbol = toCrypto.symbol,
                    rate = conversionRate
                )
            }
        }
    }
}
