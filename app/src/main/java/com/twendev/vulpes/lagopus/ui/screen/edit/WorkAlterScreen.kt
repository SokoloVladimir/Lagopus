package com.twendev.vulpes.lagopus.ui.screen.edit

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.NumberPicker
import com.twendev.vulpes.lagopus.R
import com.twendev.vulpes.lagopus.model.Discipline
import com.twendev.vulpes.lagopus.model.Semester
import com.twendev.vulpes.lagopus.model.Work
import com.twendev.vulpes.lagopus.model.WorkType
import com.twendev.vulpes.lagopus.ui.NavigationManager
import com.twendev.vulpes.lagopus.ui.TopAppBarElement
import com.twendev.vulpes.lagopus.ui.component.circleloading.CircleLoading
import com.twendev.vulpes.lagopus.ui.component.dropdown.OutlinedDropdown
import com.twendev.vulpes.lagopus.ui.viewmodel.LoadingUiState
import com.twendev.vulpes.lagopus.ui.viewmodel.WorkAlterViewModel
import kotlin.math.roundToInt

@Composable
fun WorkAlterScreen(
    setTopAppBar: (@Composable (NavigationManager) -> Unit) -> Unit,
    snackBarHostState: SnackbarHostState,
    navigateBack: () -> Unit,
    workId: Int
) {
    Log.d("WorkAlterScreen", "Opened")
    setTopAppBar {
        TopAppBarElement(
            titleRes = R.string.screen_workalter_process,
            navManager = it
        )
    }

    val viewModel by remember { mutableStateOf(WorkAlterViewModel(workId)) }
    val loadingUiState = viewModel.loadingUiState.collectAsState()
    val uiState = viewModel.uiState.collectAsState()

    WorkAlterScreenContent(
        loadingUiState = loadingUiState.value,
        item = uiState.value.item,
        disciplines = uiState.value.disciplines,
        workTypes = uiState.value.workTypes,
        semesters = uiState.value.semesters,
        onItemUpdate = {
            viewModel.updateItem(it)
        },
        onItemSave = {
            viewModel.saveItem()
            navigateBack()
        },
        onItemDelete = {
            viewModel.deleteItem()
            navigateBack()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WorkAlterScreenContent(
    loadingUiState : LoadingUiState,
    item : Work,
    disciplines : List<Discipline>,
    workTypes : List<WorkType>,
    semesters : List<Semester>,
    onItemUpdate: (Work) -> Unit,
    onItemSave: () -> Unit,
    onItemDelete: () -> Unit
) {
    if (loadingUiState.isLoading) {
        Column(
            modifier = Modifier.padding(15.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircleLoading()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text ="Работа #${item.id}")
                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = item.theme,
                    label = { Text(text = "Тема") },
                    singleLine = true,
                    onValueChange = { onItemUpdate(item.copy(theme = it)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))

                OutlinedDropdown(
                    items = disciplines,
                    onSelect = {
                        onItemUpdate(item.copy(discipline = it, disciplineId = it.id))
                    },
                    placeholder = "Дисциплина",
                    preselectedItem = item.discipline,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))

                OutlinedDropdown(
                    items = workTypes,
                    onSelect = {
                        onItemUpdate(item.copy(workType = it, workTypeId = it.id))
                    },
                    placeholder = "Тип работы",
                    preselectedItem = item.workType,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))

                OutlinedDropdown(
                    items = semesters,
                    onSelect = {
                        onItemUpdate(item.copy(semester = it, semesterId = it.id))
                    },
                    placeholder = "Семестр",
                    preselectedItem = item.semester,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Номер")
                        NumberPicker(
                            value = item.number,
                            onValueChange = { onItemUpdate(item.copy(number = it)) },
                            range = 1..100
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Заданий")
                        NumberPicker(
                            value = item.taskCount,
                            onValueChange = {
                                onItemUpdate(
                                    item.copy(
                                        taskCount = it,
                                        taskFor3 = percentageWithRound(it, 0.6),
                                        taskFor4 = percentageWithRound(it, 0.75),
                                        taskFor5 = percentageWithRound(it, 0.9),
                                    )
                                )
                            },
                            range = 1..64
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "На 3")
                        NumberPicker(
                            value = item.taskFor3,
                            onValueChange = { onItemUpdate(item.copy(taskFor3 = it)) },
                            range = 1..item.taskCount
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "На 4")
                        NumberPicker(
                            value = item.taskFor4,
                            onValueChange = { onItemUpdate(item.copy(taskFor4 = it)) },
                            range = 1..item.taskCount
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "На 5")
                        NumberPicker(
                            value = item.taskFor5,
                            onValueChange = { onItemUpdate(item.copy(taskFor5 = it)) },
                            range = 1..item.taskCount
                        )
                    }
                }
            }


            Row(
                modifier = Modifier
                    .weight(1f, false)
                    .fillMaxWidth()
            ) {
                if (item.id != 0) {
                    OutlinedButton(
                        onClick = { onItemDelete() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Outlined.Delete, contentDescription = "delete")
                        Text("Удалить")
                    }
                }
                OutlinedButton(
                    onClick = { onItemSave() },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = "save")
                    Text("Сохранить")
                }
            }
        }
    }
}

fun percentageWithRound(n: Int, percentage: Double) : Int {
    return (n * percentage).roundToInt()
}