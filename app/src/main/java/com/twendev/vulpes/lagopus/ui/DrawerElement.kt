package com.twendev.vulpes.lagopus.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DrawerElement(
    navManager: NavigationManager,
) {
    DrawerElementContent {
        ScaffoldElement(navManager = navManager)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DrawerElementContent(
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerContent = { DrawerContent() },
        content = content
    )
}

@Composable
private fun DrawerContent() {
    Text(text = "drawer")
}