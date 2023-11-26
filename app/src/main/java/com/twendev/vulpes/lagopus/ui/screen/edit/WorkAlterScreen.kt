package com.twendev.vulpes.lagopus.ui.screen.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.twendev.vulpes.lagopus.model.Discipline
import com.twendev.vulpes.lagopus.model.Semester
import com.twendev.vulpes.lagopus.model.Work
import com.twendev.vulpes.lagopus.model.WorkType
import com.twendev.vulpes.lagopus.ui.component.circleloading.CircleLoading
import com.twendev.vulpes.lagopus.ui.component.searchabledropdown.SearchableDropdown
import com.twendev.vulpes.lagopus.ui.component.searchabledropdown.SearchableDropdownController
import com.twendev.vulpes.lagopus.ui.viewmodel.LoadingUiState
import com.twendev.vulpes.lagopus.ui.viewmodel.WorkAlterViewModel

@Composable
fun WorkAlterScreen(padding: PaddingValues, snackBarHostState: SnackbarHostState, workId: Int) {
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
        }
    )
}

@Composable
fun WorkAlterScreenContent(
    loadingUiState : LoadingUiState,
    item : Work,
    disciplines : List<Discipline>,
    workTypes : List<WorkType>,
    semesters : List<Semester>,
    onItemUpdate: (Work) -> Unit
) {
    val disciplineController = SearchableDropdownController(disciplines, selected = item.discipline)
    val workTypeController = SearchableDropdownController(workTypes, selected = item.workType)
    val semesterController = SearchableDropdownController(semesters, selected = item.semester)

    disciplineController.onSelect = {
        onItemUpdate(item.copy(discipline = it, disciplineId = it.id))
    }
    workTypeController.onSelect = {
        onItemUpdate(item.copy(workType = it, workTypeId = it.id))
    }
    semesterController.onSelect = {
        onItemUpdate(item.copy(semester = it, semesterId = it.id))
    }

    if (loadingUiState.isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircleLoading()
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text ="Запись #${item.id}")
            SearchableDropdown(placeholder = "Дисциплина", controller = disciplineController)
            SearchableDropdown(placeholder = "Тип работы", controller = workTypeController)
            SearchableDropdown(placeholder = "Семестр", controller = semesterController)
        }
    }
}