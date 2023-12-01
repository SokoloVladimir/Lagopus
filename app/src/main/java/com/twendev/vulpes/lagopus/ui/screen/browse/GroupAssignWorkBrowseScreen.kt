package com.twendev.vulpes.lagopus.ui.screen.browse

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.twendev.vulpes.lagopus.ui.viewmodel.GroupBrowseViewModel
import com.twendev.vulpes.lagopus.ui.viewmodel.WorkBrowseViewModel

@Composable
fun GroupAssignWorkBrowseScreen(snackBarHostState: SnackbarHostState, onItemClick: (Int) -> Unit) {
    Log.d("GroupAssignWorkBrowseScreen",  "Opened")

    val viewModel by remember { mutableStateOf(WorkBrowseViewModel()) }
    val loadingUiState = viewModel.loadingUiState.collectAsState()
    val uiState = viewModel.uiState.collectAsState()

    WorkBrowseScreenContent(
        loadingUiState = loadingUiState.value,
        uiState = uiState.value,
        viewModel = viewModel,
        snackBarHostState = snackBarHostState,
        filterByDiscipline = {
            viewModel.filterByDiscipline(it)
        },
        filterByWorkType = {
            viewModel.filterByWorkType(it)
        },
        filterBySemester = {
            viewModel.filterBySemester(it)
        },
        onItemClick = onItemClick,
    )
}