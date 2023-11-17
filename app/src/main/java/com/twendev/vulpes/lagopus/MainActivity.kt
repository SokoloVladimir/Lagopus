package com.twendev.vulpes.lagopus

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.twendev.vulpes.lagopus.ui.screen.AuthScreen
import com.twendev.vulpes.lagopus.ui.screen.MainScreen
import com.twendev.vulpes.lagopus.ui.screen.Screen
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
                    val navController = rememberNavController()
                    val screens = listOf(Screen.MainScreen, Screen.AuthScreen)
                    val navBackStackEntry by navController.currentBackStackEntryAsState()

                    ModalNavigationDrawer(
                        drawerContent = {
                            Text(text = "drawer")
                        }
                    ) {
                        Scaffold(
                            snackbarHost = { SnackbarHost(snackbarHostState) },
                            bottomBar = {
                                if (navBackStackEntry?.destination?.route != Screen.AuthScreen.route) {
                                    NavigationBar {
                                        val currentDestination = navBackStackEntry?.destination
                                        screens.forEach { screen ->
                                            NavigationBarItem(
                                                icon = {
                                                    Icon(
                                                        Icons.Filled.Favorite,
                                                        contentDescription = null
                                                    )
                                                },
                                                label = { Text(stringResource(screen.resourceId)) },
                                                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                                onClick = {
                                                    navController.navigate(screen.route) {
                                                        popUpTo(navController.graph.startDestinationId) {
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
                            NavHost(navController = navController, startDestination = Screen.AuthScreen.route) {
                                composable(
                                    route = Screen.AuthScreen.route
                                ) {
                                    AuthScreen(innerPadding = innerPadding, showMessage = displaySnackBar, navigateToMainScreen = { url ->
                                        try {
                                            if (!url.contains("http")) {
                                                throw IllegalArgumentException("wrong URL")
                                            }

                                            Log.d("MA", Screen.MainScreen.createRoute(url))
                                            ZerdaService(url)
                                            navController.navigate(Screen.MainScreen.createRoute(url))
                                            true
                                        } catch (ex : Exception) {
                                            Log.d("MA:ex", ex.message ?: "empty")
                                            false
                                        }
                                    })
                                }
                                composable(
                                    route = Screen.MainScreen.createRoute("{url}"),
                                    arguments = listOf(navArgument(name = "url") { type = NavType.StringType })
                                ) {
                                    MainScreen(padding = innerPadding, instanceUrl = it.arguments?.getString("url"))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
