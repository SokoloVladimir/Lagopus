package com.twendev.vulpes.lagopus.ui.screen.browse

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.twendev.vulpes.lagopus.R
import com.twendev.vulpes.lagopus.model.Discipline
import com.twendev.vulpes.lagopus.model.Semester
import com.twendev.vulpes.lagopus.model.Work
import com.twendev.vulpes.lagopus.model.WorkType
import com.twendev.vulpes.lagopus.ui.NavigationManager
import com.twendev.vulpes.lagopus.ui.TopAppBarElement
import com.twendev.vulpes.lagopus.ui.component.dropdown.OutlinedDropdown
import com.twendev.vulpes.lagopus.ui.viewmodel.WorkBrowseUiState
import com.twendev.vulpes.lagopus.ui.viewmodel.WorkBrowseViewModel

@Composable
fun WorkBrowseScreen(
    setTopAppBar: (@Composable (NavigationManager) -> Unit) -> Unit,
    onItemClick: (Work) -> Unit
) {
    Log.d("WorkBrowseScreen",  "Opened")
    setTopAppBar {
        TopAppBarElement(
            titleRes = R.string.screen_workbrowse_process,
            navManager = it
        )
    }

    val viewModel by remember { mutableStateOf(WorkBrowseViewModel(onItemClick)) }

    WorkBrowseScreenContent(
        uiState = viewModel.uiState.collectAsState(),
        items = viewModel.items,
        filterByDiscipline = {
            viewModel.filterByDiscipline(it)
        },
        filterByWorkType = {
            viewModel.filterByWorkType(it)
        },
        filterBySemester = {
            viewModel.filterBySemester(it)
        },
        onItemClick = viewModel.onItemClick,
        loadableCompose = {
            viewModel.WithLoadable(it)
        }
    )
}

@Composable
fun WorkBrowseScreenContent(
    uiState: State<WorkBrowseUiState>,
    items: List<Work>,
    filterByDiscipline : (Discipline) -> Unit,
    filterByWorkType : (WorkType) -> Unit,
    filterBySemester : (Semester) -> Unit,
    onItemClick: (Work) -> Unit,
    loadableCompose: @Composable (@Composable () -> Unit) -> Unit,
) {
    loadableCompose {
        Column {
            var isFilterExpanded by remember { mutableStateOf(false) }

            Column(Modifier.padding(15.dp)) {
                OutlinedButton(onClick = {
                    isFilterExpanded = !isFilterExpanded
                }) {
                    Text(
                        text = "Фильтрация:",
                        modifier = Modifier.padding(0.dp, 10.dp)
                    )
                }
                AnimatedVisibility(visible = isFilterExpanded) {
                    Column {
                        // TODO: завести string resources
                        // TODO: переписать dropdown на resetable/добавить рядом кнопку сброса
                        OutlinedDropdown(items = uiState.value.disciplines, onSelect = filterByDiscipline, placeholder = "Дисциплина")
                        OutlinedDropdown(items = uiState.value.workTypes, onSelect = filterByWorkType, placeholder = "Тип работы")
                        OutlinedDropdown(items = uiState.value.semesters, onSelect = filterBySemester, placeholder = "Семестр")
                    }
                }
            }

            Divider(color = Color.Black, thickness = 1.dp)

            LazyColumn(
                contentPadding = PaddingValues(15.dp)
            ) {

                items(items) { item ->
                    if (
                        !isFilterExpanded ||
                        (uiState.value.selectedDiscipline == null || uiState.value.selectedDiscipline == item.discipline)
                        && (uiState.value.selectedWorkType == null || uiState.value.selectedWorkType == item.workType)
                    ) {
                        WorkCard(
                            item = item,
                            onClick = {
                                onItemClick(item)
                            }
                        )
                        Spacer(Modifier.height(15.dp))
                    }
                }

                item {
                    IconButton(
                        onClick = {
                            onItemClick(Work())
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row {
                            Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
                            Text(text = "Новая запись")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WorkCard(
    item : Work,
    onClick: () -> Unit
) {
    WorkCardContent(
        item = item,
        onClick = onClick
    )
}

@Composable
fun WorkCardContent(
    item: Work,
    onClick : () -> Unit
) {
    OutlinedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
            .clickable {
                onClick()
            }
    ) {
        Column( Modifier.padding(15.dp)) {
            Text(
                text = item.toString(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}