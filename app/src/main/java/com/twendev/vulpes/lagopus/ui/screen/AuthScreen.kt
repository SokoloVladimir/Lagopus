package com.twendev.vulpes.lagopus.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import com.twendev.vulpes.lagopus.ui.component.circleloading.CircleLoading
import com.twendev.vulpes.lagopus.ui.component.searchabledropdown.SearchableDropdown
import com.twendev.vulpes.lagopus.ui.component.searchabledropdown.SearchableDropdownController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    innerPadding: PaddingValues,
    navigateToMainScreen : suspend (instance: String) -> Boolean,
    showMessage : (message: String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var status : Boolean? by remember { mutableStateOf(null) }
    val instanceController by remember { mutableStateOf(
        SearchableDropdownController(
            list = listOf("https://zerda.twenkey.ru")
        )
    )}
    val tryToConnect : () -> Unit = {
        scope.launch {
            status = true
            delay(500)
            status = navigateToMainScreen(instanceController.uiState.value.selectedText)
            if (status == false) {
                showMessage("Failed to fetch API")
            }
        }
    }
    instanceController.onSelect = { tryToConnect() }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (status == true) {
            CircleLoading()
        } else {
            SearchableDropdown(placeholder = "Instance of API", controller = instanceController)

            Button(
                onClick = tryToConnect,
                shape = RectangleShape
            ) {
                Text("Log in")
            }
        }
    }
}
