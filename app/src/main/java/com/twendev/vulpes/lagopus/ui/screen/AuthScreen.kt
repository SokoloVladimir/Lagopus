package com.twendev.vulpes.lagopus.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.twendev.vulpes.lagopus.R
import com.twendev.vulpes.lagopus.ui.NavigationManager
import com.twendev.vulpes.lagopus.ui.TopAppBarElement
import com.twendev.vulpes.lagopus.ui.component.circleloading.CircleLoading
import com.twendev.vulpes.lagopus.ui.component.searchabledropdown.SearchableDropdown
import com.twendev.vulpes.lagopus.ui.component.searchabledropdown.SearchableDropdownController
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    setTopAppBar: (@Composable (NavigationManager) -> Unit) -> Unit,
    navigateToMainScreen : suspend (instance: String) -> Boolean,
    showMessage : suspend (message: String) -> Unit
) {
    Log.d("AuthScreen", "Opened")
    setTopAppBar {
        TopAppBarElement(
            titleRes = R.string.screen_auth_process,
            navManager = it,
            isBackable = false
        )
    }

    // TODO: прибраться в AuthScreen, унести в AuthViewModel
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    var loadingStatus by remember { mutableStateOf(false) }
    var login by remember { mutableStateOf("") }
    var passw by remember { mutableStateOf("") }

    val instanceController by remember { mutableStateOf(
        SearchableDropdownController(
            list = listOf("https://zerda.twenkey.ru")
        )
    )}
    val tryToConnect : () -> Unit = {
        scope.launch {
            loadingStatus = navigateToMainScreen(instanceController.uiState.value.selectedText)
            if (!loadingStatus) {
                showMessage("Ошибка подключения к API")
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
            SearchableDropdown(placeholder = "Инстанс API", controller = instanceSearchableDropdownController)
            OutlinedTextField(
                value = login,
                onValueChange = onLoginChange,
                label = { Text("Логин") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {
                    onLoginKeyboardAction()
                }),
                singleLine = true
            )
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Пароль") },
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
                Text("Вход")
            }
        }
    }
}
