package com.twendev.vulpes.lagopus.ui.screen.browse

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.twendev.vulpes.lagopus.model.Result
import com.twendev.vulpes.lagopus.model.Work
import com.twendev.vulpes.lagopus.ui.component.checkbox.InternalCheckBox
import com.twendev.vulpes.lagopus.ui.component.circleloading.CircleLoading
import com.twendev.vulpes.lagopus.ui.viewmodel.LoadingStatus
import com.twendev.vulpes.lagopus.ui.viewmodel.LoadingUiState
import com.twendev.vulpes.lagopus.ui.viewmodel.ResultBrowseViewModel

@Composable
fun ResultBrowseScreen(snackBarHostState: SnackbarHostState, groupId: Int, workId: Int) {
    Log.d("ResultBrowseScreen",  "Opened")

    val viewModel by remember { mutableStateOf(ResultBrowseViewModel(groupId = groupId, workId = workId)) }
    val loadingUiState = viewModel.loadingUiState.collectAsState()

    ResultBrowseScreenContent(
        loadingUiState = loadingUiState.value,
        viewModel = viewModel,
        snackBarHostState = snackBarHostState,
        items = viewModel.items,
        work = viewModel.work
    )
}

@Composable
fun ResultBrowseScreenContent(
    loadingUiState : LoadingUiState,
    viewModel: ResultBrowseViewModel,
    snackBarHostState : SnackbarHostState,
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
            Column {
                LazyColumn(
                    contentPadding = PaddingValues(15.dp)
                ) {
                    items(viewModel.items) { item ->
                        ResultCard(
                            item = item,
                            taskCount = work!!.taskCount,
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
fun ResultCard(
    item : Result,
    setTaskState: (Int, Boolean) -> Unit,
    taskCount: Int,
    onSave: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    ResultCardContent(
        item = item,
        setTaskState = setTaskState,
        taskCount = taskCount,
        isExpanded = isExpanded,
        onChangeExpanded = {
            isExpanded = !isExpanded
            if (!isExpanded) {
                onSave()
            }
        }
    )
}

@Composable
fun ResultCardContent(
    item: Result,
    setTaskState: (Int, Boolean) -> Unit,
    taskCount: Int,
    isExpanded: Boolean,
    onChangeExpanded: () -> Unit,
) {
    OutlinedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color.Black),
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
        Column(
            Modifier.padding(9.dp)
        ) {
            Text(
                text = item.cachedStudent.toString(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            LazyRow(
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(taskCount) { index ->
                    Box(
                        modifier = Modifier.padding(5.dp)
                    ) {
                        InternalCheckBox(
                            value = " " + (index + 1).toString() + " ",
                            checked = item.getTaskState(index),
                            onCheckedChange = {
                                setTaskState(index, it)
                            },
                            enabled = isExpanded
                        )
                    }


                    /*Text(text = (index + 1).toString())
                    Checkbox(
                        enabled = isExpanded,
                        checked = item.getTaskState(index),
                        onCheckedChange = {
                            setTaskState(index, it)
                        }
                    )*/
                }
            }
        }
    }
}