package com.twendev.vulpes.lagopus.ui.screen.browse

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.NumberPicker
import com.twendev.vulpes.lagopus.model.Semester
import com.twendev.vulpes.lagopus.ui.component.circleloading.CircleLoading
import com.twendev.vulpes.lagopus.ui.viewmodel.LoadingUiState
import com.twendev.vulpes.lagopus.ui.viewmodel.SemesterBrowseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SemesterBrowseScreen(padding: PaddingValues, snackBarHostState: SnackbarHostState) {
    Log.d("SemesterBrowseScreen",  "Opened")

    val viewModel by remember { mutableStateOf(SemesterBrowseViewModel()) }
    val uiState = viewModel.loadingUiState.collectAsState()
    val scope = rememberCoroutineScope()

    SemesterBrowseScreenContent(
        uiState = uiState,
        viewModel = viewModel,
        snackBarHostState = snackBarHostState,
        coroutineScope = scope
    )
}

@Composable
fun SemesterBrowseScreenContent(
    uiState : State<LoadingUiState>,
    viewModel: SemesterBrowseViewModel,
    snackBarHostState : SnackbarHostState,
    coroutineScope : CoroutineScope
) {
    Box {
        if (uiState.value.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircleLoading()
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(15.dp)
        ) {
            items(viewModel.items) { item ->
                SemesterCard(
                    item = item,
                    onYearChange = {
                        val index = viewModel.items.indexOf(item)
                        viewModel.items[index] = viewModel.items[index].copy(startYear = it)
                    },
                    onIsSecondChange = {
                        val index = viewModel.items.indexOf(item)
                        viewModel.items[index] = viewModel.items[index].copy(isSecond = it)
                    },
                    onSave = {
                        viewModel.updateItem(it)
                    },
                    onDelete = {
                        viewModel.prepareDeleteItem(it)
                        coroutineScope.launch {
                            val snackBarResult = snackBarHostState.showSnackbar(
                                message = "Запись ${it.id} удалена",
                                actionLabel = "Undo",
                                duration = SnackbarDuration.Short
                            )
                            when (snackBarResult) {
                                SnackbarResult.ActionPerformed -> {
                                    Log.d("DisciplineEditScreen", "snackBar ActionPerformed")
                                    viewModel.cancelDeleteItem(it)
                                }
                                SnackbarResult.Dismissed -> {
                                    Log.d("DisciplineEditScreen", "snackBar Dismissed")
                                    viewModel.confirmDeleteItem(it)
                                }
                            }
                        }
                    }
                )
                Spacer(Modifier.height(15.dp))
            }

            item {
                IconButton(
                    onClick = {
                        viewModel.createItem(Semester())
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

@Composable
fun SemesterCard(
    item : Semester,
    onYearChange : (Int) -> Unit,
    onIsSecondChange: (Boolean) -> Unit,
    onSave: suspend (Semester) -> Unit,
    onDelete: suspend (Semester) -> Unit
) {
    var editMode by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    SemesterCardContent(
        item = item,
        isEditMode = editMode,
        onEditModeSwitch = {
            editMode = !editMode
            if (!editMode) {
                // выход из режима редактирования
                scope.launch {
                    onSave(item)
                }
            }
        },
        onYearChange = onYearChange,
        onIsSecondChange = onIsSecondChange,
        onDeleteClick = {
            editMode = false
            scope.launch {
                onDelete(item)
            }
        }
    )
}

@Composable
fun SemesterCardContent(
    item: Semester,
    isEditMode : Boolean,
    onEditModeSwitch : () -> Unit,
    onYearChange: (Int) -> Unit,
    onIsSecondChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit,
    yearMin: Int = 2010,
    yearMax: Int = 2100
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
                onEditModeSwitch()
            }
    ) {
        AnimatedVisibility(
            visible = !isEditMode,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Text(
                text = item.toString(),
                modifier = Modifier.padding(15.dp)
            )
        }
        AnimatedVisibility(
            visible = isEditMode,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
        ) {
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Text("Редактирование записи #${item.id}")
                Text("Учебный год")
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NumberPicker(
                        value = item.startYear,
                        range = yearMin..yearMax,
                        onValueChange = onYearChange
                    )
                    Text("-")
                    NumberPicker(
                        value = item.startYear + 1,
                        range = yearMin + 1..yearMax + 1,
                        onValueChange = {
                            onYearChange(it - 1)
                        }
                    )
                }
                Text(text = "Семестр")
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = !item.isSecond,
                        onClick = {
                            onIsSecondChange(false)
                        }
                    )
                    Text("1")

                    Spacer(Modifier.width(20.dp))

                    RadioButton(
                        selected = item.isSecond,
                        onClick = {
                            onIsSecondChange(true)
                        }
                    )
                    Text("2")
                }

                IconButton(onClick = onDeleteClick) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "delete")
                }
            }
        }
    }
}