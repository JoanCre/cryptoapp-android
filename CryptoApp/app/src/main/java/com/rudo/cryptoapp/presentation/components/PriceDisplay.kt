package com.rudo.cryptoapp.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.rudo.cryptoapp.presentation.util.NumberFormatters
import java.math.BigDecimal

@Composable
fun PriceDisplay(
    price: BigDecimal?,
    percentChange: Double?
) {
    Text(
        text = NumberFormatters.formatUsd(price),
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold
    )
    percentChange?.let { change ->
        val formatted = NumberFormatters.formatPercent(change)
        Text(
            text = formatted ?: "",
            color = if (change >= 0) Color(0xFF34C759) else MaterialTheme.colorScheme.error,
            fontSize = 14.sp
        )
    }
}

@Composable
fun CompactPriceDisplay(
    price: BigDecimal,
    percentChange: Double?
) {
    Column(
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = NumberFormatters.formatUsd(price),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1
        )
        percentChange?.let { change ->
            val formatted = NumberFormatters.formatPercent(change)
            Text(
                text = formatted ?: "",
                color = if (change >= 0) Color(0xFF34C759) else MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                maxLines = 1
            )
        }
    }
}
