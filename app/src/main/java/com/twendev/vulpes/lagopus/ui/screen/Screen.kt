package com.twendev.vulpes.lagopus.ui.screen

import androidx.compose.material.icons.Icons
 import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector) {
    object MainScreen : Screen("main", Icons.Filled.Home)
    object AuthScreen : Screen("auth", Icons.Filled.Lock)
    object DisciplineBrowseScreen : Screen("disciplinebrowse", Icons.Filled.Edit)
    object WorkTypeBrowseScreen : Screen("worktypebrowse", Icons.Filled.Build)
    object SemesterBrowseScreen : Screen("semesterbrowse", Icons.Filled.Info)
    object WorkBrowseScreen : Screen("workbrowse", Icons.Filled.PlayArrow)
    object WorkAlterScreen : Screen("workalter", Icons.Filled.AddCircle)

    fun createWithInstance(url : String?) : String {
        return "$route?instance=$url"
    }
    fun createWithId(id : Int) : String {
        return "$route?id=$id"
    }
    override fun toString(): String {
        return route
    }
}