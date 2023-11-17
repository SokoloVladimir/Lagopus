package com.twendev.vulpes.lagopus.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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

    val focusManager = LocalFocusManager.current

    var loadingStatus by remember { mutableStateOf(false) }
    var login by remember { mutableStateOf("") }
    var passw by remember { mutableStateOf("") }

    val instanceController by remember { mutableStateOf(
        SearchableDropdownController(
            list = listOf("https://zerda.twenkey.ru", "http://200.0.0.54:5205")
        )
    )}
    val tryToConnect : () -> Unit = {
        scope.launch {
            loadingStatus = true
            delay(500)
            loadingStatus = navigateToMainScreen(instanceController.uiState.value.selectedText)
            if (!loadingStatus) {
                showMessage("Failed to fetch API")
            }
        }
    }
    instanceController.onSelect = { focusManager.moveFocus(FocusDirection.Down) }

    AuthScreenContent(
        isLoading = loadingStatus,
        instanceSearchableDropdownController = instanceController,
        login = login,
        password = passw,
        onLoginChange = { login = it },
        onPasswordChange = { passw = it },
        onLogin = tryToConnect,
        onLoginKeyboardAction = {
            focusManager.moveFocus(FocusDirection.Down)
        },
        onPasswordKeyBoardAction = tryToConnect

    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> AuthScreenContent(
    isLoading : Boolean,
    instanceSearchableDropdownController: SearchableDropdownController<T>,
    login : String,
    password : String,
    onLoginChange : (String) -> Unit,
    onPasswordChange : (String) -> Unit,
    onLoginKeyboardAction : () -> Unit,
    onPasswordKeyBoardAction : () -> Unit,
    onLogin : () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            CircleLoading()
        } else {
            SearchableDropdown(placeholder = "Instance of API", controller = instanceSearchableDropdownController)
            OutlinedTextField(
                value = login,
                onValueChange = onLoginChange,
                label = { Text("Login") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {
                    onLoginKeyboardAction()
                }),
                singleLine = true
            )
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go, keyboardType = KeyboardType.Password),
                keyboardActions = KeyboardActions(onGo = {
                    onPasswordKeyBoardAction()
                }),
                singleLine = true
            )
            Button(
                onClick = onLogin,
                shape = RectangleShape
            ) {
                Text("Log in")
            }
        }
    }
}
