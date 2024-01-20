package com.twendev.vulpes.lagopus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.twendev.vulpes.lagopus.ui.DrawerElement
import com.twendev.vulpes.lagopus.ui.rememberNavManager
import com.twendev.vulpes.lagopus.ui.theme.LagopusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LagopusTheme(darkTheme = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    startKoinWithAndroidContext(this@MainActivity)
                    DrawerElement(rememberNavManager())
                }
            }
        }
    }
}