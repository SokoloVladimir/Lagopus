package com.twendev.vulpes.lagopus.ui

import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.twendev.vulpes.lagopus.datasource.ZerdaService
import com.twendev.vulpes.lagopus.ui.screen.AuthScreen
import com.twendev.vulpes.lagopus.ui.screen.DerivativeScreen
import com.twendev.vulpes.lagopus.ui.screen.Screen
import com.twendev.vulpes.lagopus.ui.screen.StudentResultScreen
import com.twendev.vulpes.lagopus.ui.screen.browse.DisciplineBrowseScreen
import com.twendev.vulpes.lagopus.ui.screen.browse.GroupBrowseScreen
import com.twendev.vulpes.lagopus.ui.screen.browse.ResultBrowseScreen
import com.twendev.vulpes.lagopus.ui.screen.browse.SemesterBrowseScreen
import com.twendev.vulpes.lagopus.ui.screen.browse.StudentBrowseScreen
import com.twendev.vulpes.lagopus.ui.screen.browse.WorkBrowseScreen
import com.twendev.vulpes.lagopus.ui.screen.browse.WorkTypeBrowseScreen
import com.twendev.vulpes.lagopus.ui.screen.edit.AssignmentAlterScreen
import com.twendev.vulpes.lagopus.ui.screen.edit.WorkAlterScreen

@Composable
fun Navigation(
    navManager: NavigationManager,
    snackbarHostState: SnackbarHostState,
    setAppBar: (@Composable (NavigationManager) -> Unit) -> Unit
) {
    NavHost(navController = navManager.navController, startDestination = Screen.AuthScreen.route) {
        composable(
            route = Screen.AuthScreen.route
        ) {
            AuthScreen(
                setTopAppBar = setAppBar,
                showMessage = {
                    snackbarHostState.showSnackbar(message = it, duration = SnackbarDuration.Short)
                },
                resolveAuth = { url, login, password ->
                    try {
                        if (!url.contains("http")) {
                            throw IllegalArgumentException("wrong URL")
                        }

                        ZerdaService.Singleton = ZerdaService(url)
                        ZerdaService.Singleton.bearer = ZerdaService.Singleton.api.getBearer(login, password)

                        if (ZerdaService.Singleton.bearer?.role == "teacher") {
                            navManager.navTo(Screen.DerivativeScreen.route)
                        } else {
                            navManager.navTo(Screen.StudentResultScreen.route)
                        }

                        true
                    } catch (ex : Exception) {
                        Log.d("MA:ex", ex.message ?: "empty")
                        false
                    }
                }
            )
        }
        composable(
            route = Screen.DerivativeScreen.route
        ) {
            DerivativeScreen(
                setTopAppBar = setAppBar,
                onBrowseDisciplines = {
                    navManager.navTo(Screen.DisciplineBrowseScreen.route)
                },
                onBrowseWorkTypes = {
                    navManager.navTo(Screen.WorkTypeBrowseScreen.route)
                },
                onBrowseSemesters = {
                    navManager.navTo(Screen.SemesterBrowseScreen.route)
                }
            )
        }
        composable(
            route = Screen.DisciplineBrowseScreen.route
        ) {
            DisciplineBrowseScreen(
                setTopAppBar = setAppBar,
                snackBarHostState = snackbarHostState
            )
        }
        composable(
            route = Screen.WorkTypeBrowseScreen.route
        ) {
            WorkTypeBrowseScreen(
                setTopAppBar = setAppBar,
                snackBarHostState = snackbarHostState
            )
        }
        composable(
            route = Screen.WorkBrowseScreen.route
        ) {
            WorkBrowseScreen(
                setTopAppBar = setAppBar,
                onItemClick = {
                    navManager.navTo(Screen.WorkAlterScreen.createWithId(it.id))
                }
            )
        }
        composable(
            route = Screen.GroupAssignWorkBrowse.withGroupId(),
            arguments = listOf(navArgument("groupId") {
                nullable = false
                type = NavType.IntType
            })
        ) { navStackEntry ->
            val groupId = navStackEntry.arguments?.getInt("groupId")

            WorkBrowseScreen(
                setTopAppBar = setAppBar,
                onItemClick = { work ->
                    navManager.navTo(Screen.AssignmentAlterScreen.createWithGroupIdAndWorkid(groupId!!, work.id))
                }
            )
        }
        composable(
            route = Screen.GroupResultsWorkBrowse.withGroupId(),
            arguments = listOf(navArgument("groupId") {
                nullable = false
                type = NavType.IntType
            })
        ) {navStackEntry ->
            val groupId = navStackEntry.arguments?.getInt("groupId")

            WorkBrowseScreen(
                setTopAppBar = setAppBar,
                onItemClick = { work ->
                    navManager.navTo(
                        Screen.ResultBrowseScreen.createWithGroupIdAndWorkid(
                            groupId = groupId!!,
                            workId = work.id
                        )
                    )
                }
            )
        }
        composable(
            route = Screen.SemesterBrowseScreen.route
        ) {
            SemesterBrowseScreen(
                setTopAppBar = setAppBar,
                snackBarHostState = snackbarHostState
            )
        }
        composable(
            route = Screen.GroupBrowseScreen.route
        ) {
            GroupBrowseScreen(
                setTopAppBar = setAppBar,
                snackBarHostState = snackbarHostState,
                onAssign = {
                    navManager.navTo(Screen.GroupAssignWorkBrowse.createWithGroupId(it.id))
                },
                onBrowseStudents = {
                    navManager.navTo(Screen.StudentBrowseScreen.createWithGroupId(it.id))
                },
                onBrowseWorkResults = {
                    navManager.navTo(Screen.GroupResultsWorkBrowse.createWithGroupId(it.id))
                }
            )
        }
        composable(
            route = Screen.WorkAlterScreen.withId(),
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            })
        ) {
            WorkAlterScreen(
                setTopAppBar = setAppBar,
                snackBarHostState = snackbarHostState,
                navigateBack = {
                    navManager.navUp()
                },
                workId = it.arguments?.getInt("id") ?: -1
            )
        }
        composable(
            route = Screen.StudentBrowseScreen.withGroupId(),
            arguments = listOf(navArgument("groupId") {
                type = NavType.IntType
            })
        ) { navStackEntry ->
            val groupId = navStackEntry.arguments?.getInt("groupId")
            StudentBrowseScreen(
                setTopAppBar = setAppBar,
                groupId = groupId!!,
                onItemClick = {
                    // TODO: student alter screen
                }
            )
        }
        composable(
            route = Screen.ResultBrowseScreen.withGroupIdAndWorkId(),
            arguments = listOf(
                navArgument("workId") {
                    type = NavType.IntType
                },
                navArgument("groupId") {
                    type = NavType.IntType
                }
            )
        ) { navStackEntry ->
            val workId = navStackEntry.arguments?.getInt("workId")
            val groupId = navStackEntry.arguments?.getInt("groupId")
            ResultBrowseScreen(
                setTopAppBar = setAppBar,
                workId = workId!!,
                groupId = groupId!!
            )
        }
        composable(
            route = Screen.AssignmentAlterScreen.withGroupIdAndWorkId(),
            arguments = listOf(
                navArgument("workId") {
                    type = NavType.IntType
                },
                navArgument("groupId") {
                    type = NavType.IntType
                }
            )
        ) { navStackEntry ->
            val workId = navStackEntry.arguments?.getInt("workId")
            val groupId = navStackEntry.arguments?.getInt("groupId")
            AssignmentAlterScreen(
                setTopAppBar = setAppBar,
                workId = workId!!,
                groupId = groupId!!,
                onConfirm = {
                    navManager.navTo(Screen.GroupBrowseScreen.route) {
                        popUpTo(navManager.navController.graph.startDestinationId) {
                            saveState = false
                        }
                    }
                }
            )
        }
        composable(
            route = Screen.StudentResultScreen.route
        ) {
            StudentResultScreen(
                setTopAppBar = setAppBar
            )
        }
    }
}