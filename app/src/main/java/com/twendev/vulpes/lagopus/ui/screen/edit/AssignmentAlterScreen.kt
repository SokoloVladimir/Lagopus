package com.twendev.vulpes.lagopus.ui.screen.edit

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.twendev.vulpes.lagopus.model.Assignment
import com.twendev.vulpes.lagopus.model.Group
import com.twendev.vulpes.lagopus.model.Work
import com.twendev.vulpes.lagopus.model.WorkType
import com.twendev.vulpes.lagopus.ui.component.circleloading.CircleLoading
import com.twendev.vulpes.lagopus.ui.theme.LagopusTheme
import com.twendev.vulpes.lagopus.ui.viewmodel.AssignmentAlterUiState
import com.twendev.vulpes.lagopus.ui.viewmodel.AssignmentAlterViewModel
import com.twendev.vulpes.lagopus.ui.viewmodel.LoadingStatus
import com.twendev.vulpes.lagopus.ui.viewmodel.LoadingUiState

@Composable
fun AssignmentAlterScreen(
    workId: Int,
    groupId: Int,
    onConfirm: () -> Unit
) {
    Log.d("AssignemtnAlterScreen", "Opened")

    val viewModel by remember { mutableStateOf(
        AssignmentAlterViewModel(
            workId = workId,
            groupId = groupId
        )
    ) }

    AssignmentAlterScreenContent(
        loadingUiState = viewModel.loadingUiState.collectAsState(),
        uiState = viewModel.uiState.collectAsState(),
        onItemSave = {
            viewModel.saveItem()
            onConfirm()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentAlterScreenContent(
    loadingUiState: State<LoadingUiState>,
    uiState: State<AssignmentAlterUiState>,
    onItemSave: () -> Unit
) {
    if (loadingUiState.value.isLoading) {
        Column(
            modifier = Modifier.padding(15.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircleLoading()
        }
    } else {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            OutlinedTextField(
                value = uiState.value.work.toString(),
                onValueChange = {},
                label = { Text(text = "Работа") },
                readOnly = true
            )
            OutlinedTextField(
                value = uiState.value.group.toString(),
                onValueChange = {},
                label = { Text(text = "Группа") },
                readOnly = true
            )
            // TODO: создать datepicker (вставка с датой назначения)
            OutlinedButton(onClick = onItemSave) {
                Text("Подтвердить")
            }
        }
    }
}

@Preview
@Composable
fun AssignmentAlterScreenContent_Preview() {
    LagopusTheme(
        darkTheme = false
    ) {
        val loadingUiState = remember { mutableStateOf(
            LoadingUiState(
                loading = LoadingStatus.None
            )
        ) }
        val uiState = remember { mutableStateOf(
            AssignmentAlterUiState(
                item = Assignment(
                    workId = 0,
                    groupId = 0
                ),
                work = Work(
                    id = 0,
                    number = 3,
                    workType = WorkType(
                        name = "Самостятельная работа"
                    ),
                    theme = "Разработка навигации"),
                group = Group(
                    id = 0,
                    name = "КК-13"
                )
            )
        )}

        AssignmentAlterScreenContent(
            loadingUiState = loadingUiState,
            uiState = uiState,
            onItemSave = {}
        )
    }
}