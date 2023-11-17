package com.twendev.vulpes.lagopus.ui.screen

import androidx.annotation.StringRes

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object MainScreen : Screen("main", androidx.compose.ui.R.string.tab)
    object AuthScreen : Screen("auth", androidx.compose.material3.R.string.dialog)

    fun createRoute(url : String?) : String {
        return "$route?instance=$url"
    }
    override fun toString(): String {
        return route
    }
}