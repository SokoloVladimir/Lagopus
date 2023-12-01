package com.twendev.vulpes.lagopus.ui.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val icon: ImageVector
) {
    object MainScreen : Screen(
        route = "main",
        icon = Icons.Filled.Home
    )
    object AuthScreen : Screen(
        route = "auth",
        icon = Icons.Filled.Lock
    )
    object DisciplineBrowseScreen : Screen(
        route ="disciplinebrowse",
        icon = Icons.Filled.Edit
    )
    object WorkTypeBrowseScreen : Screen(
        route = "worktypebrowse",
        icon = Icons.Filled.Build
    )
    object SemesterBrowseScreen : Screen(
        route = "semesterbrowse",
        icon = Icons.Filled.Info
    )
    object WorkBrowseScreen : Screen(
        route = "workbrowse",
        icon = Icons.Filled.PlayArrow
    )
    object WorkAlterScreen : Screen(
        route = "workalter",
        icon = Icons.Filled.AddCircle
    )
    object GroupBrowseScreen : Screen(
        route = "groupbrowse",
        icon = Icons.Filled.ArrowBack
    )
    object GroupAssignWorkBrowse : Screen (
        route = "groupassignworkbrowse",
        icon = Icons.Filled.ArrowDropDown
    )
    object GroupResultsWorkBrowse : Screen (
        route = "groupresultsworkbrowse",
        icon = Icons.Filled.ArrowDropDown
    )
    object StudentBrowseScreen : Screen (
        route = "studentbrowsescreen",
        icon = Icons.Filled.AccountCircle
    )
    object ResultBrowseScreen : Screen (
        route = "resultbrowsescreen",
        icon = Icons.Filled.Favorite
    )
    object NotFound : Screen(
        route = "notfound",
        icon = Icons.Filled.Lock
    )

    fun createWithInstance(url : String?) : String {
        return "$route?instance=$url"
    }
    fun createWithId(id : Int) : String {
        return "$route?id=$id"
    }
    fun createWithGroupId(groupId: Int) : String {
        return "$route?groupId=$groupId"
    }
    override fun toString(): String {
        return route
    }
}