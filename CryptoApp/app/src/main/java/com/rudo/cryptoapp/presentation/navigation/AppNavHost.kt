package com.rudo.cryptoapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rudo.cryptoapp.presentation.screens.swap.SwapScreen

/**
 * AppNavHost handles the main navigation flow of the application.
 */
@Composable
fun AppNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = CryptoScreens.SwapScreen
    ) {
        composable<CryptoScreens.SwapScreen> {
            SwapScreen()
        }
    }
}