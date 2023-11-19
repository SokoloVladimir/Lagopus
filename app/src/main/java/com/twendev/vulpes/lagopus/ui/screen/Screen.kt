package com.twendev.vulpes.lagopus.ui.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector) {
    object MainScreen : Screen("main", Icons.Filled.Home)
    object AuthScreen : Screen("auth", Icons.Filled.Lock)
    object DisciplineViewScreen : Screen("disciplineview", Icons.Filled.Edit)

    fun createRoute(url : String?) : String {
        return "$route?instance=$url"
    }
    override fun toString(): String {
        return route
    }
}