package com.example.news.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.news.presentation.view.DetailNewsView
import com.example.news.presentation.view.HomeView

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.HOMESCREEN.path) {
        composable(Routes.HOMESCREEN.path) {
            HomeView(navController = navController)
        }
        composable(
            Routes.NEWS_DETAIL.path,
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("url") { type = NavType.StringType },
            ),
        ) {
            DetailNewsView(navController = navController)
        }
    }
}