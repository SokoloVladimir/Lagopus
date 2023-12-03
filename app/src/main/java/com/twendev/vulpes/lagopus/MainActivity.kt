package com.twendev.vulpes.lagopus

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.twendev.vulpes.lagopus.datasource.ZerdaService
import com.twendev.vulpes.lagopus.ui.screen.*
import com.twendev.vulpes.lagopus.ui.screen.browse.DisciplineBrowseScreen
import com.twendev.vulpes.lagopus.ui.screen.browse.GroupBrowseScreen
import com.twendev.vulpes.lagopus.ui.screen.browse.ResultBrowseScreen
import com.twendev.vulpes.lagopus.ui.screen.browse.SemesterBrowseScreen
import com.twendev.vulpes.lagopus.ui.screen.browse.StudentBrowseScreen
import com.twendev.vulpes.lagopus.ui.screen.browse.WorkBrowseScreen
import com.twendev.vulpes.lagopus.ui.screen.browse.WorkTypeBrowseScreen
import com.twendev.vulpes.lagopus.ui.screen.edit.*
import com.twendev.vulpes.lagopus.ui.theme.LagopusTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LagopusTheme(darkTheme = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val scope : CoroutineScope = rememberCoroutineScope()
                    val snackbarHostState = remember { SnackbarHostState() }
                    val displaySnackBar : (text: String) -> Unit = { text ->
                        scope.launch {
                            snackbarHostState.showSnackbar(message = text, duration = SnackbarDuration.Short)
                        }
                    }
                    val navManager = rememberNavManager()
                    val screens = listOf(
                        Screen.MainScreen,
                        Screen.DisciplineBrowseScreen,
                        Screen.WorkTypeBrowseScreen,
                        Screen.WorkBrowseScreen,
                        Screen.SemesterBrowseScreen,
                        Screen.GroupBrowseScreen
                    )
                    val navBackStackEntry by navManager.collectNavStackEntryAsState()
                    var topAppBar : (@Composable () -> Unit) by remember { mutableStateOf({ }) }
                    val changeTopAppBar : (@Composable () -> Unit) -> Unit = { compose ->
                        topAppBar = compose
                    }


                    ModalNavigationDrawer(
                        drawerContent = { Text(text = "drawer") }
                    ) {
                        Scaffold(
                            snackbarHost = { SnackbarHost(snackbarHostState) },
                            topBar = topAppBar,
                            bottomBar = {
                                if (navBackStackEntry?.destination?.route != Screen.AuthScreen.route) {
                                    NavigationBar {
                                        val currentDestination = navBackStackEntry?.destination
                                        screens.forEach { screen ->
                                            NavigationBarItem(
                                                icon = {
                                                    Icon(
                                                        screen.icon,
                                                        contentDescription = null
                                                    )
                                                },
                                                label = { Text(screen.route) },
                                                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                                onClick = {
                                                    navManager.navTo(screen.route) {
                                                        popUpTo(navManager.navController.graph.startDestinationId) {
                                                            saveState = true
                                                        }
                                                        launchSingleTop = true
                                                        restoreState = true
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        ) { innerPadding ->
                            Box(
                                Modifier.padding(innerPadding)
                            ) {
                                NavHost(navController = navManager.navController, startDestination = Screen.AuthScreen.route) {
                                    composable(
                                        route = Screen.AuthScreen.route
                                    ) {
                                        AuthScreen(showMessage = displaySnackBar, navigateToMainScreen = { url ->
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
                                        })
                                    }
                                    composable(
                                        route = Screen.MainScreen.route
                                    ) {
                                        MainScreen()
                                    }
                                    composable(
                                        route = Screen.DisciplineBrowseScreen.route
                                    ) {
                                        DisciplineBrowseScreen(snackbarHostState)
                                    }
                                    composable(
                                        route = Screen.WorkTypeBrowseScreen.route
                                    ) {
                                        WorkTypeBrowseScreen(snackbarHostState)
                                    }
                                    composable(
                                        route = Screen.WorkBrowseScreen.route
                                    ) {
                                        WorkBrowseScreen {
                                            navManager.navTo(Screen.WorkAlterScreen.createWithId(it.id))
                                        }
                                    }
                                    composable(
                                        route = Screen.GroupAssignWorkBrowse.withGroupId(),
                                        arguments = listOf(
                                            navArgument("groupId") {
                                                nullable = false
                                                type = NavType.IntType
                                            }
                                        )
                                    ) { navStackEntry ->
                                        val groupId = navStackEntry.arguments?.getInt("groupId")

                                        WorkBrowseScreen(
                                            onItemClick = { work ->
                                                navManager.navTo(Screen.AssignmentAlterScreen.createWithGroupIdAndWorkid(groupId!!, work.id))
                                            }
                                        )
                                    }
                                    composable(
                                        route = Screen.GroupResultsWorkBrowse.withGroupId(),
                                        arguments = listOf(
                                            navArgument("groupId") {
                                                nullable = false
                                                type = NavType.IntType
                                            }
                                        )
                                    ) {navStackEntry ->
                                        val groupId = navStackEntry.arguments?.getInt("groupId")

                                        WorkBrowseScreen(
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
                                        SemesterBrowseScreen(snackbarHostState)
                                    }
                                    composable(
                                        route = Screen.GroupBrowseScreen.route
                                    ) {
                                        GroupBrowseScreen(
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
                                        arguments = listOf(
                                            navArgument("id") {
                                                type = NavType.IntType
                                            }
                                        )
                                    ) {
                                        WorkAlterScreen(
                                            snackBarHostState = snackbarHostState,
                                            navigateBack = {
                                                navManager.navUp()
                                            },
                                            workId = it.arguments?.getInt("id") ?: -1
                                        )
                                    }
                                    composable(
                                        route = Screen.StudentBrowseScreen.withGroupId(),
                                        arguments = listOf(
                                            navArgument("groupId") {
                                                type = NavType.IntType
                                            }
                                        )
                                    ) { navStackEntry ->
                                        val groupId = navStackEntry.arguments?.getInt("groupId")
                                        StudentBrowseScreen(
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
                                            workId = workId!!,
                                            groupId = groupId!!
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Stable
class NavigationManager(val navController : NavHostController) {
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