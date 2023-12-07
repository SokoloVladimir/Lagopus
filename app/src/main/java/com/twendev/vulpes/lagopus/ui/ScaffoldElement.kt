package com.twendev.vulpes.lagopus.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.twendev.vulpes.lagopus.datasource.ZerdaService
import com.twendev.vulpes.lagopus.ui.screen.Screen

@Composable
fun ScaffoldElement(
    navManager: NavigationManager
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val navBackStackEntry by navManager.collectNavStackEntryAsState()
    var topAppBar : (@Composable (NavigationManager) -> Unit) by remember { mutableStateOf({ }) }

    ScaffoldElementContent(
        snackbarHostState = snackbarHostState,
        topAppBar = { topAppBar(navManager) },
        bottomAppBar = {
            if (navBackStackEntry?.destination?.route != Screen.AuthScreen.route
                && ZerdaService.Singleton.bearer?.role == "teacher") {
                ScaffoldElementBottomBar(
                    navManager = navManager,
                    destination = navBackStackEntry?.destination
                )
            }
        }
    ) {
        Navigation(
            navManager = navManager,
            snackbarHostState = snackbarHostState,
            setAppBar = { compose ->
                topAppBar = compose
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScaffoldElementContent(
    snackbarHostState: SnackbarHostState,
    topAppBar: (@Composable () -> Unit),
    bottomAppBar: (@Composable () -> Unit),
    content: (@Composable () -> Unit)
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = topAppBar,
        bottomBar = bottomAppBar
    ) { innerPadding ->
        Box(
            Modifier.padding(innerPadding)
        ) {
            content()
        }
    }
}

@Composable
private fun ScaffoldElementBottomBar(navManager: NavigationManager, destination: NavDestination?) {
    val items = listOf(
        Screen.DerivativeScreen,
        Screen.WorkBrowseScreen,
        Screen.GroupBrowseScreen
    )

    NavigationBar {
        for (item in items) {
            NavigationBarItem(
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = null
                    )
                },
                label = { Text(item.route) },
                selected = destination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navManager.navTo(item.route) {
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