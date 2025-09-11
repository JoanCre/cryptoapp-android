package com.rudo.cryptoapp.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.rudo.cryptoapp.presentation.util.NumberFormatters
import java.math.BigDecimal

@Composable
fun ConversionRateText(
    fromSymbol: String,
    toSymbol: String,
    rate: BigDecimal,
    modifier: Modifier = Modifier
) {
    val formattedRate = NumberFormatters.formatConversionRate(rate)

    Text(
        text = "1 $fromSymbol = $formattedRate $toSymbol",
        color = MaterialTheme.colorScheme.primary,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth()
    )
}
