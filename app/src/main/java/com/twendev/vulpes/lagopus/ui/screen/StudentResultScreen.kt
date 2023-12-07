package com.twendev.vulpes.lagopus.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.twendev.vulpes.lagopus.model.Result
import com.twendev.vulpes.lagopus.ui.NavigationManager
import com.twendev.vulpes.lagopus.ui.TopAppBarElement
import com.twendev.vulpes.lagopus.ui.component.circleloading.CircleLoading
import com.twendev.vulpes.lagopus.ui.screen.browse.ResultCardContent
import com.twendev.vulpes.lagopus.ui.viewmodel.LoadingUiState
import com.twendev.vulpes.lagopus.ui.viewmodel.StudentResultViewModel

@Composable
fun StudentResultScreen(
    setTopAppBar: (@Composable (NavigationManager) -> Unit) -> Unit
) {
    Log.d("AuthScreen", "Opened")
    setTopAppBar {
        TopAppBarElement(
            title = "Персональные результаты",
            navManager = it
        )
    }

    val viewModel by remember { mutableStateOf(StudentResultViewModel()) }

    StudentResultScreenContent(
        loadingUiState = viewModel.loadingUiState.collectAsState(),
        items = viewModel.items
    )
}

@Composable
fun StudentResultScreenContent(
    loadingUiState: State<LoadingUiState>,
    items: List<Result>
) {
    if (loadingUiState.value.isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircleLoading()
        }
    } else {
        LazyColumn(
            Modifier.padding(15.dp)
        ) {
            items(items) { item ->
                ResultCardContent(
                    header = item.cachedWork.toString(),
                    item = item,
                    work = item.cachedWork!!,
                    isExpanded = false,
                    isEdited = false,
                    setTaskState = { i, b -> },
                    onChangeExpanded = { }
                )
                Spacer(Modifier.height(15.dp))
            }
        }
    }
}
