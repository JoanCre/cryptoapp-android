package com.rudo.cryptoapp.domain.repository

import com.rudo.cryptoapp.domain.entities.Cryptocurrency

interface CryptoRepository {
    suspend fun getCryptocurrencies(): List<Cryptocurrency>
}
