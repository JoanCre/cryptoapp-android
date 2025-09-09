package com.rudo.cryptoapp.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Composable
fun ConversionRateText(
    fromSymbol: String,
    toSymbol: String,
    rate: BigDecimal,
    modifier: Modifier = Modifier
) {
    val rateFormatter = DecimalFormat("0.0000", DecimalFormatSymbols(Locale.US))

    Text(
        text = "1 $fromSymbol = ${rateFormatter.format(rate.toDouble())} $toSymbol",
        color = MaterialTheme.colorScheme.primary,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth()
    )
}
