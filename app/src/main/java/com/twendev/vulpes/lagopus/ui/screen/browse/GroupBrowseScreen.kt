package com.twendev.vulpes.lagopus.ui.screen.browse

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.twendev.vulpes.lagopus.model.Group
import com.twendev.vulpes.lagopus.ui.component.circleloading.CircleLoading
import com.twendev.vulpes.lagopus.ui.viewmodel.GroupBrowseViewModel
import com.twendev.vulpes.lagopus.ui.viewmodel.LoadingUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun GroupBrowseScreen(snackBarHostState: SnackbarHostState, onAssign: (Group) -> Unit, onBrowseStudents: (Group) -> Unit) {
    Log.d("GroupBrowseScreen",  "Opened")

    val viewModel by remember { mutableStateOf(GroupBrowseViewModel()) }
    val uiState = viewModel.loadingUiState.collectAsState()
    val scope = rememberCoroutineScope()

    GroupBrowseScreenContent(
        uiState = uiState,
        viewModel = viewModel,
        snackBarHostState = snackBarHostState,
        coroutineScope = scope,
        onAssign = onAssign,
        onBrowseStudents = onBrowseStudents
    )
}

@Composable
fun GroupBrowseScreenContent(
    uiState : State<LoadingUiState>,
    viewModel: GroupBrowseViewModel,
    snackBarHostState : SnackbarHostState,
    coroutineScope : CoroutineScope,
    onAssign: (Group) -> Unit,
    onBrowseStudents: (Group) -> Unit
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
                GroupCard(
                    item = item,
                    onNameChange = {
                        val index = viewModel.items.indexOf(item)
                        viewModel.items[index] = viewModel.items[index].copy(name = it)
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
                                    Log.d("GroupEditScreen", "snackBar ActionPerformed")
                                    viewModel.cancelDeleteItem(it)
                                }
                                SnackbarResult.Dismissed -> {
                                    Log.d("GroupEditScreen", "snackBar Dismissed")
                                    viewModel.confirmDeleteItem(it)
                                }
                            }
                        }
                    },
                    onAssign = onAssign,
                    onBrowseStudents = onBrowseStudents
                )
                Spacer(Modifier.height(15.dp))
            }

            item {
                IconButton(
                    onClick = {
                        viewModel.createItem(Group())
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
fun GroupCard(
    item : Group,
    onNameChange : (String) -> Unit,
    onSave: suspend (Group) -> Unit,
    onDelete: suspend (Group) -> Unit,
    onAssign: (Group) -> Unit,
    onBrowseStudents: (Group) -> Unit
) {
    var editMode by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val scope = rememberCoroutineScope()

    GroupCardContent(
        item = item,
        focusRequester = focusRequester,
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
        onNameChange = onNameChange,
        onDeleteClick = {
            editMode = false
            scope.launch {
                onDelete(item)
            }
        },
        onAssignClick = {
            onAssign(item)
        },
        onBrowseStudentsClick = {
            onBrowseStudents(item)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun GroupCardContent(
    item: Group,
    focusRequester: FocusRequester,
    isEditMode : Boolean,
    onEditModeSwitch : () -> Unit,
    onNameChange: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onAssignClick: () -> Unit,
    onBrowseStudentsClick: () -> Unit
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
                text = item.name,
                modifier = Modifier.padding(15.dp)
            )
        }
        AnimatedVisibility(
            visible = isEditMode,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
        ) {
            if (this.transition.currentState == this.transition.targetState) {
                focusRequester.requestFocus()
            }
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Text("Редактирование записи #${item.id}")
                OutlinedTextField(
                    value = item.name,
                    onValueChange = onNameChange,
                    label = { Text("Наименование типа") },
                    singleLine = true,
                    keyboardActions = KeyboardActions(onDone = { onEditModeSwitch() }),
                    modifier = Modifier.focusRequester(focusRequester)
                )
                Row {
                    OutlinedButton(onClick = onDeleteClick) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "delete")
                        Text(text = "Удалить")
                    }
                    OutlinedButton(onClick = onAssignClick) {
                        Icon(imageVector = Icons.Filled.DateRange, contentDescription = "assign")
                        Text(text = "Назначить работу")
                    }
                }
                OutlinedButton(onClick = onBrowseStudentsClick) {
                    Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = "students")
                    Text(text = "Студенты")
                }
            }
        }
    }
}