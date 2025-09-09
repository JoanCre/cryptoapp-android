package com.rudo.cryptoapp.presentation.navigation

import kotlinx.serialization.Serializable

sealed class CryptoScreens {
    @Serializable
    data object SwapScreen : CryptoScreens()
}
