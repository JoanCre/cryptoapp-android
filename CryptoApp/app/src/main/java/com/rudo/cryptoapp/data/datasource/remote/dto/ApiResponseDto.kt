package com.rudo.cryptoapp.data.datasource.remote.dto

import com.google.gson.annotations.SerializedName

data class ApiResponseDto(
    @SerializedName("data")
    val data: List<CryptocurrencyDto>,
    @SerializedName("status")
    val status: StatusDto
)

data class CryptocurrencyDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("cmc_rank")
    val cmcRank: Int?,
    @SerializedName("num_market_pairs")
    val numMarketPairs: Int?,
    @SerializedName("circulating_supply")
    val circulatingSupply: Double?,
    @SerializedName("total_supply")
    val totalSupply: Double?,
    @SerializedName("max_supply")
    val maxSupply: Double?,
    @SerializedName("infinite_supply")
    val infiniteSupply: Boolean?,
    @SerializedName("last_updated")
    val lastUpdated: String?,
    @SerializedName("date_added")
    val dateAdded: String?,
    @SerializedName("tags")
    val tags: List<String>?,
    @SerializedName("platform")
    val platform: PlatformDto?,
    @SerializedName("quote")
    val quote: Map<String, QuoteDto>
)

data class PlatformDto(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("slug")
    val slug: String?,
    @SerializedName("token_address")
    val tokenAddress: String?
)

data class QuoteDto(
    @SerializedName("price")
    val price: Double,
    @SerializedName("volume_24h")
    val volume24h: Double?,
    @SerializedName("volume_change_24h")
    val volumeChange24h: Double?,
    @SerializedName("percent_change_1h")
    val percentChange1h: Double?,
    @SerializedName("percent_change_24h")
    val percentChange24h: Double?,
    @SerializedName("percent_change_7d")
    val percentChange7d: Double?,
    @SerializedName("market_cap")
    val marketCap: Double?,
    @SerializedName("market_cap_dominance")
    val marketCapDominance: Double?,
    @SerializedName("fully_diluted_market_cap")
    val fullyDilutedMarketCap: Double?,
    @SerializedName("last_updated")
    val lastUpdated: String?
)

data class StatusDto(
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("error_code")
    val errorCode: Int,
    @SerializedName("error_message")
    val errorMessage: String?,
    @SerializedName("elapsed")
    val elapsed: Int,
    @SerializedName("credit_count")
    val creditCount: Int
)
