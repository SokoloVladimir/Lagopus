package com.twendev.vulpes.lagopus.ui.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val icon: ImageVector
) {
    sealed class Groupable(route: String, icon: ImageVector) : Screen(route, icon) {
        fun withGroupId(): String {
            return "$route?groupId={groupId}"
        }

        fun createWithGroupId(groupId: Int): String {
            return "$route?groupId=$groupId"
        }
    }

    sealed class WorkableGroupable(route: String, icon: ImageVector) : Screen(route, icon) {
        fun withGroupIdAndWorkId(): String {
            return "$route?groupId={groupId}&workId={workId}"
        }

        fun createWithGroupIdAndWorkid(groupId: Int, workId: Int): String {
            return "$route?groupId=$groupId&workId=$workId"
        }
    }

    object DerivativeScreen : Screen(
        route = "derivative",
        icon = Icons.Filled.List
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
        icon = Icons.Filled.DateRange
    )
    object WorkAlterScreen : Screen(
        route = "workalter",
        icon = Icons.Filled.AddCircle
    )
    object GroupBrowseScreen : Screen(
        route = "groupbrowse",
        icon = Icons.Filled.Person
    )
    object GroupAssignWorkBrowse : Screen.Groupable (
        route = "groupassignworkbrowse",
        icon = Icons.Filled.ArrowDropDown
    )
    object GroupResultsWorkBrowse : Screen.Groupable (
        route = "groupresultsworkbrowse",
        icon = Icons.Filled.ArrowDropDown
    )
    object StudentBrowseScreen : Screen.Groupable (
        route = "studentbrowsescreen",
        icon = Icons.Filled.AccountCircle
    )
    object ResultBrowseScreen : Screen.WorkableGroupable (
        route = "resultbrowsescreen",
        icon = Icons.Filled.Favorite
    )
    object AssignmentAlterScreen : Screen.WorkableGroupable(
        route = "assignmentalterscreen",
        icon = Icons.Filled.Lock
    )
    object NotFound : Screen(
        route = "notfound",
        icon = Icons.Filled.Lock
    )

    fun withId() : String {
        return "$route?id={id}"
    }
    fun createWithId(id : Int) : String {
        return "$route?id=$id"
    }

    override fun toString(): String {
        return route
    }
}