package com.rudo.cryptoapp.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.rudo.cryptoapp.presentation.screens.swap.SwapScreen

fun NavGraphBuilder.cryptoNavGraph(
    navController: NavHostController
) {
    val navigateBack: () -> Unit = { navController.navigateUp() }
    
    composable<CryptoScreens.SwapScreen> {
        SwapScreen()
    }
}
