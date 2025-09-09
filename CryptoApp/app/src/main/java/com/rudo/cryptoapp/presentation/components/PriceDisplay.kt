package com.rudo.cryptoapp.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Composable
fun PriceDisplay(
    price: BigDecimal?,
    percentChange: Double?
) {
    val priceFormatter = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.US))
    val percentFormatter = DecimalFormat("0.00", DecimalFormatSymbols(Locale.US))

    Text(
        text = price?.let { "$${priceFormatter.format(it.toDouble())}" } ?: "$0.00",
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold
    )
    percentChange?.let { change ->
        Text(
            text = "${if (change >= 0) "+" else ""}${percentFormatter.format(change)}%",
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
            text = "$${String.format(Locale.US, "%.2f", price.toDouble())}",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1
        )
        percentChange?.let { change ->
            Text(
                text = "${if (change >= 0) "+" else ""}${String.format(Locale.US, "%.2f", change)}%",
                color = if (change >= 0) Color(0xFF34C759) else MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                maxLines = 1
            )
        }
    }
}
