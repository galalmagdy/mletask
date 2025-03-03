package com.example.mletask.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.mletask.presentation.ui.HomeScreen
import androidx.navigation.compose.composable
import com.example.mletask.presentation.ui.ExperienceDetailsScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("details/{id}") { backStackEntry ->
            ExperienceDetailsScreen(
                id = backStackEntry.arguments?.getString("id") ?: "",
                onBack = { navController.popBackStack() }
            )
        }
    }
}