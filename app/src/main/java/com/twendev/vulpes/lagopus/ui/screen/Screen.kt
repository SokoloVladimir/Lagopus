package com.twendev.vulpes.lagopus.ui.screen

import androidx.annotation.StringRes
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
import com.twendev.vulpes.lagopus.R

sealed class Screen(
    val route: String,
    val icon: ImageVector,
    @StringRes val nameRes: Int = 0
) {
    sealed class Groupable(route: String, icon: ImageVector, nameRes: Int = 0) : Screen(route, icon, nameRes) {
        fun withGroupId(): String {
            return "$route?groupId={groupId}"
        }

        fun createWithGroupId(groupId: Int): String {
            return "$route?groupId=$groupId"
        }
    }

    sealed class WorkableGroupable(route: String, icon: ImageVector, nameRes: Int = 0) : Screen(route, icon, nameRes) {
        fun withGroupIdAndWorkId(): String {
            return "$route?groupId={groupId}&workId={workId}"
        }

        fun createWithGroupIdAndWorkid(groupId: Int, workId: Int): String {
            return "$route?groupId=$groupId&workId=$workId"
        }
    }

    object DerivativeScreen : Screen(
        route = "derivative",
        icon = Icons.Filled.List,
        nameRes = R.string.screen_derivative_fullname
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
        icon = Icons.Filled.DateRange,
        nameRes = R.string.screen_workbrowse_fullname
    )
    object WorkAlterScreen : Screen(
        route = "workalter",
        icon = Icons.Filled.AddCircle
    )
    object GroupBrowseScreen : Screen(
        route = "groupbrowse",
        icon = Icons.Filled.Person,
        nameRes = R.string.screen_groupbrowse_fullname
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
        route = "studentbrowse",
        icon = Icons.Filled.AccountCircle
    )
    object ResultBrowseScreen : Screen.WorkableGroupable (
        route = "resultbrowse",
        icon = Icons.Filled.Favorite
    )
    object AssignmentAlterScreen : Screen.WorkableGroupable(
        route = "assignmentalter",
        icon = Icons.Filled.Lock
    )
    object StudentResultScreen : Screen(
        route = "studentresult",
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