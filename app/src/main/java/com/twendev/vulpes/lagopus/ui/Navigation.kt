package com.twendev.vulpes.lagopus.ui

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.twendev.vulpes.lagopus.R
import com.twendev.vulpes.lagopus.datasource.ZerdaService
import com.twendev.vulpes.lagopus.ui.screen.AuthScreen
import com.twendev.vulpes.lagopus.ui.screen.MainScreen
import com.twendev.vulpes.lagopus.ui.screen.Screen
import com.twendev.vulpes.lagopus.ui.screen.browse.DisciplineBrowseScreen
import com.twendev.vulpes.lagopus.ui.screen.browse.GroupBrowseScreen
import com.twendev.vulpes.lagopus.ui.screen.browse.ResultBrowseScreen
import com.twendev.vulpes.lagopus.ui.screen.browse.SemesterBrowseScreen
import com.twendev.vulpes.lagopus.ui.screen.browse.StudentBrowseScreen
import com.twendev.vulpes.lagopus.ui.screen.browse.WorkBrowseScreen
import com.twendev.vulpes.lagopus.ui.screen.browse.WorkTypeBrowseScreen
import com.twendev.vulpes.lagopus.ui.screen.edit.AssignmentAlterScreen
import com.twendev.vulpes.lagopus.ui.screen.edit.WorkAlterScreen
import com.twendev.vulpes.lagopus.ui.theme.LagopusTheme

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
                navigateToMainScreen = { url ->
                    try {
                        if (!url.contains("http")) {
                            throw IllegalArgumentException("wrong URL")
                        }

                        ZerdaService.Singleton = ZerdaService(url)
                        navManager.navTo(Screen.MainScreen.route)
                        true
                    } catch (ex : Exception) {
                        Log.d("MA:ex", ex.message ?: "empty")
                        false
                    }
                }
            )
        }
        composable(
            route = Screen.MainScreen.route
        ) {
            MainScreen(
                setTopAppBar = setAppBar
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
    }
}



@Composable
fun TopAppBarElement(
    titleRes: Int,
    navManager: NavigationManager,
    dropdownMap: Map<String, () -> Unit>? = null,
    isBackable: Boolean = true,
    onBackClick: (() -> Unit)? = null
) {
    TopAppBarElement(
        title = LocalContext.current.getString(titleRes),
        navManager = navManager,
        dropdownMap = dropdownMap,
        isBackable = isBackable,
        onBackClick = onBackClick
    )
}

@Composable
fun TopAppBarElement(
    title: String,
    navManager: NavigationManager,
    dropdownMap: Map<String, () -> Unit>? = null,
    isBackable: Boolean = true,
    onBackClick: (() -> Unit)? = null
) {
    TopAppBarElementContent(
        title = title,
        onBackClick = onBackClick ?: { navManager.navUp() },
        itemToClick = dropdownMap ?: mapOf(),
        navigationIconVisible = isBackable
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarElementContent(
    title: String,
    onBackClick: () -> Unit,
    itemToClick: Map<String,() -> Unit>,
    navigationIconVisible: Boolean = true
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = title)
            }
        },
        navigationIcon = {
            if (navigationIconVisible) {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
                }
            }
        },
        actions = {
            if (itemToClick.isNotEmpty()) {
                IconButton(onClick = { isMenuExpanded = !isMenuExpanded }) {
                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "menu")
                    DropdownMenu(expanded = isMenuExpanded, onDismissRequest = { isMenuExpanded = false }) {
                        for (pair in itemToClick) {
                            DropdownMenuItem(text = { Text(text = pair.key) }, onClick = pair.value)
                        }
                    }
                }
            }
        }
    )
}

@Preview(name = "AuthScreen")
@Composable
fun TopAppBarElement_Preview() {
    LagopusTheme(
        darkTheme = false
    ) {
        TopAppBarElementContent(
            title = LocalContext.current.getString(R.string.screen_auth_process),
            onBackClick = { },
            itemToClick = mapOf(
                "1" to {},
                "2" to {}
            )
        )
    }
}

@Preview(name = "WorkBrowseScreen_dark")
@Composable
fun TopAppBarElement_PreviewDark() {
    LagopusTheme(
        darkTheme = true
    ) {
        TopAppBarElementContent(
            title = LocalContext.current.getString(R.string.screen_workbrowse_process),
            onBackClick = { },
            itemToClick = mapOf()
        )
    }
}