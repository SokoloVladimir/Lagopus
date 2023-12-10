package com.twendev.vulpes.lagopus.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.twendev.vulpes.lagopus.R
import com.twendev.vulpes.lagopus.ui.theme.LagopusTheme

@Composable
fun TopAppBarElement(
    @StringRes titleRes: Int,
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