package com.twendev.vulpes.lagopus.ui.screen.browse

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.twendev.vulpes.lagopus.model.Discipline
import com.twendev.vulpes.lagopus.model.Group
import com.twendev.vulpes.lagopus.model.Result
import com.twendev.vulpes.lagopus.model.Semester
import com.twendev.vulpes.lagopus.model.Student
import com.twendev.vulpes.lagopus.model.Work
import com.twendev.vulpes.lagopus.model.WorkType
import com.twendev.vulpes.lagopus.ui.NavigationManager
import com.twendev.vulpes.lagopus.ui.TopAppBarElement
import com.twendev.vulpes.lagopus.ui.component.checkbox.InternalCheckBox
import com.twendev.vulpes.lagopus.ui.component.circleloading.CircleLoading
import com.twendev.vulpes.lagopus.ui.screen.Screen
import com.twendev.vulpes.lagopus.ui.theme.LagopusTheme
import com.twendev.vulpes.lagopus.ui.viewmodel.LoadingStatus
import com.twendev.vulpes.lagopus.ui.viewmodel.LoadingUiState
import com.twendev.vulpes.lagopus.ui.viewmodel.ResultBrowseViewModel

@Composable
fun ResultBrowseScreen(
    setTopAppBar: (@Composable (NavigationManager) -> Unit) -> Unit,
    groupId: Int,
    workId: Int
) {
    Log.d("ResultBrowseScreen",  "Opened")

    val viewModel by remember { mutableStateOf(ResultBrowseViewModel(groupId = groupId, workId = workId)) }
    val loadingUiState = viewModel.loadingUiState.collectAsState()

    ResultBrowseScreenContent(
        setTopAppBar = setTopAppBar,
        loadingUiState = loadingUiState.value,
        viewModel = viewModel,
        items = viewModel.items,
        work = viewModel.work
    )
}

@Composable
fun ResultBrowseScreenContent(
    setTopAppBar: (@Composable (NavigationManager) -> Unit) -> Unit,
    loadingUiState : LoadingUiState,
    viewModel: ResultBrowseViewModel,
    items: List<Result>,
    work: Work?
) {
    Box {
        if (loadingUiState.loading != LoadingStatus.None) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircleLoading()
            }
        }

        if (loadingUiState.loading != LoadingStatus.Full) {
            setTopAppBar {
                TopAppBarElement(
                    title = "Работа ${work?.workType?.getShortName() + work?.number} у ${viewModel.group}",
                    navManager = it,
                    dropdownMap = mapOf(
                        "Редактировать работу" to { it.navTo(Screen.WorkAlterScreen.createWithId(viewModel.work?.id!!)) }
                    )
                )
            }

            Column {
                LazyColumn(
                    contentPadding = PaddingValues(15.dp)
                ) {
                    items(items) { item ->
                        ResultCardExpandable(
                            item = item,
                            work = work!!,
                            setTaskState = { taskNumber, taskState ->
                                val index = viewModel.items.indexOf(item)
                                viewModel.items[index] = viewModel.items[index].copy(tasks = item.calculateWithNewTaskState(taskNumber, taskState))
                            },
                            onSave = {
                                viewModel.updateItem(item)
                            }
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ResultCardExpandable(
    item : Result,
    work: Work,
    setTaskState: (Int, Boolean) -> Unit,
    onSave: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var tasksBeforeEditing by remember { mutableStateOf(item.tasks) }

    ResultCardContent(
        header = item.cachedStudent.toString(),
        item = item,
        work = work,
        isExpanded = isExpanded,
        isEdited = item.tasks != tasksBeforeEditing,
        setTaskState = setTaskState,
        onChangeExpanded = {
            isExpanded = !isExpanded
            if (!isExpanded && item.tasks != tasksBeforeEditing) {
                onSave()
                tasksBeforeEditing = item.tasks
            } else if (isExpanded) {
                tasksBeforeEditing = item.tasks
            }
        }
    )
}

@Composable
fun ResultCardContent(
    header: String,
    item: Result,
    work: Work,
    isExpanded: Boolean,
    isEdited : Boolean,
    setTaskState: (Int, Boolean) -> Unit,
    onChangeExpanded: () -> Unit,
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
                onChangeExpanded()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier
                    .padding(8.dp)
                    .fillMaxWidth().weight(1f, true)
            ) {
                Text(
                    text = header,
                    fontStyle = if (isEdited) FontStyle.Italic else null,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                LazyRow(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(work.taskCount) { index ->
                        Box(
                            modifier = Modifier.padding(5.dp)
                        ) {
                            InternalCheckBox(
                                value = (index + 1).toString(),
                                checked = item.getTaskState(index),
                                onCheckedChange = {
                                    setTaskState(index, it)
                                },
                                enabled = isExpanded
                            )
                        }
                    }
                }
            }
            Box(
                Modifier.padding(17.dp)
            ) {
                if (item.tasks.countOneBits() >= work.taskFor5) {
                    Text(text = "5", fontSize = 25.sp)
                }
                else if (item.tasks.countOneBits() >= work.taskFor4) {
                    Text(text = "4", fontSize = 25.sp)
                }
                else if (item.tasks.countOneBits() >= work.taskFor3) {
                    Text(text = "3", fontSize = 25.sp)
                }
                else {
                    Text(text = "2", fontSize = 25.sp)
                }

            }
        }
    }
}

@Preview
@Composable
fun ResultCardContentPreview() {
    LagopusTheme {
        ResultCardContent(
            header = Student(
                surname = "Surname",
                name = "Name",
                group = Group(),
                groupId = 0
            ).toString(),
            item = Result(
                studentId = 0,
                workId = 0,
                tasks = 4uL,
                cachedStudent = Student(
                    surname = "Surname",
                    name = "Name",
                    group = Group(),
                    groupId = 0
                )
            ),
            work = Work(
                id = 0,
                number = 5,
                theme = "Kotlin Division",
                taskCount = 5,
                taskFor3 = 5,
                taskFor4 = 4,
                taskFor5 = 5,
                discipline = Discipline(name = "Мобильная разработка"),
                semester = Semester(startYear = 2023, isSecond = true),
                workType = WorkType(name = "Практическая работа")
            ),
            isExpanded = false,
            isEdited = false,
            setTaskState = { i, b -> },
            onChangeExpanded = { }
        )
    }
}

@Preview
@Composable
fun ResultCardContentPreview_Expanded() {
    LagopusTheme {
        ResultCardContent(
            header = Student(
                surname = "Surname",
                name = "Name",
                group = Group(),
                groupId = 0
            ).toString(),
            item = Result(
                studentId = 0,
                workId = 0,
                tasks = 4uL,
                cachedStudent = Student(
                    surname = "Surname",
                    name = "Name",
                    group = Group(),
                    groupId = 0
                )
            ),
            work = Work(
                id = 0,
                number = 5,
                theme = "Kotlin Division",
                taskCount = 5,
                taskFor3 = 5,
                taskFor4 = 4,
                taskFor5 = 5,
                discipline = Discipline(name = "Мобильная разработка"),
                semester = Semester(startYear = 2023, isSecond = true),
                workType = WorkType(name = "Практическая работа")
            ),
            isExpanded = true,
            isEdited = false,
            setTaskState = { i, b -> },
            onChangeExpanded = { }
        )
    }
}