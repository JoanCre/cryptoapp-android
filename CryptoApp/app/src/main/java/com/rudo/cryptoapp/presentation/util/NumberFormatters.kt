package com.rudo.cryptoapp.presentation.util

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object NumberFormatters {
    private val usSymbols = DecimalFormatSymbols(Locale.US)

    fun formatUsd(amount: BigDecimal?): String {
        if (amount == null) return "$0"
        val abs = amount.abs()
        return if (abs >= BigDecimal.ONE) {
            val df = DecimalFormat("#,##0.##", usSymbols)
            "$${df.format(amount.toDouble())}"
        } else {
            val scaled = amount.setScale(8, RoundingMode.HALF_UP).stripTrailingZeros()
            val plain = scaled.toPlainString()
            val normalized = if (plain.startsWith(".")) "0$plain" else plain
            "$${normalized}"
        }
    }

    fun formatPercent(percent: Double?): String? {
        percent ?: return null
        val df = DecimalFormat("0.##", usSymbols)
        val sign = if (percent >= 0) "+" else ""
        return "$sign${df.format(percent)}%"
    }

    fun formatConversionRate(rate: BigDecimal): String {
        if (rate.compareTo(BigDecimal.ZERO) == 0) return "0"
        val threshold = BigDecimal("0.00000001")
        if (rate < threshold) return "< 0.00000001"

        return if (rate < BigDecimal.ONE) {
            rate.setScale(8, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString()
        } else {
            val df = DecimalFormat("#,##0.####", usSymbols)
            df.format(rate.toDouble())
        }
    }
}
