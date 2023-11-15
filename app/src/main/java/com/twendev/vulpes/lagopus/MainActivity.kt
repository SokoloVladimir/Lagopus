package com.twendev.vulpes.lagopus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.twendev.vulpes.lagopus.ui.pages.MainPage
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
                    val scope : CoroutineScope = rememberCoroutineScope();
                    val snackbarHostState = remember { SnackbarHostState() }

                    val displaySnackBar : (text: String) -> Unit = { text ->
                        scope.launch {
                            snackbarHostState.showSnackbar(text)
                        }
                    }

                    Scaffold(
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        floatingActionButton = {
                            var clickCount by remember { mutableStateOf(0) }
                            ExtendedFloatingActionButton(
                                onClick = { displaySnackBar("${++clickCount}") },
                            ) { Text("Show snackbar") }
                        },
                        content = { innerPadding ->
                            MainPage(innerPadding, displaySnackBar)
                        }
                    )
                }
            }
        }
    }
}
