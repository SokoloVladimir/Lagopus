package com.twendev.vulpes.lagopus.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Stable
class NavigationManager(val navController : NavHostController) {
    // TODO: Имплементировать сокрытие клавиши Назад на TopAppBar
    var isBackable = mutableStateOf(true)

    fun navTo(route: String, navOptionsBuilder: NavOptionsBuilder.() -> Unit) = navController.navigate(route, navOptionsBuilder)
    fun navTo(route: String) {
        navController.navigate(route)
    }
    fun navUp() {
        navController.navigateUp()
    }

    @Composable
    fun collectNavStackEntryAsState() : State<NavBackStackEntry?> {
        return navController.currentBackStackEntryAsState()
    }
}

@Composable
fun rememberNavManager(navController: NavHostController = rememberNavController()) : NavigationManager {
    return remember(Unit) {
        NavigationManager(navController = navController)
    }
}