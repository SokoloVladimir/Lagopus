package com.twendev.vulpes.lagopus.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.twendev.vulpes.lagopus.R
import com.twendev.vulpes.lagopus.ui.NavigationManager
import com.twendev.vulpes.lagopus.ui.TopAppBarElement

@Composable
fun DerivativeScreen(
    setTopAppBar: (@Composable (NavigationManager) -> Unit) -> Unit,
    onBrowseDisciplines: () -> Unit,
    onBrowseWorkTypes: () -> Unit,
    onBrowseSemesters: () -> Unit
) {
    Log.d("DerivativeScreen", "Opened")
    setTopAppBar {
        TopAppBarElement(
            titleRes = R.string.screen_derivative_process,
            navManager = it
        )
    }

    DerivativeScreenContent(
        browseDisciplines = onBrowseDisciplines,
        browseWorkTypes = onBrowseWorkTypes,
        browseSemesters = onBrowseSemesters
    )
}

@Composable
fun DerivativeScreenContent(
    browseDisciplines: () -> Unit,
    browseWorkTypes: () -> Unit,
    browseSemesters: () -> Unit
) {
    Column(
        Modifier.padding(15.dp)
    ) {
        Button(onClick = browseDisciplines) {
            Text("К экрану дисциплин")
        }
        Spacer(Modifier.width(30.dp))

        Button(onClick = browseWorkTypes) {
            Text("К экрану типов работ")
        }
        Spacer(Modifier.width(30.dp))

        Button(onClick = browseSemesters) {
            Text("К экрану семестров")
        }
        Spacer(Modifier.width(30.dp))
    }
}

@Preview
@Composable
fun DerivativeScreenContentPreview() {
    DerivativeScreenContent(
        browseDisciplines = { },
        browseWorkTypes = { },
        browseSemesters = { }
    )
}
