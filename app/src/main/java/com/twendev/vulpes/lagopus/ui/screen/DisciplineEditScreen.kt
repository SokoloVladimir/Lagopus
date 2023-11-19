package com.twendev.vulpes.lagopus.ui.screen

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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.twendev.vulpes.lagopus.model.Discipline
import com.twendev.vulpes.lagopus.ui.component.circleloading.CircleLoading
import com.twendev.vulpes.lagopus.ui.viewmodel.DisciplineEditUiState
import com.twendev.vulpes.lagopus.ui.viewmodel.DisciplineEditViewModel

@Composable
fun DisciplineEditScreen(padding: PaddingValues) {
    Log.d("DisciplineViewScreen",  "Opened")

    val viewModel by remember { mutableStateOf(DisciplineEditViewModel()) }
    val uiState = viewModel.uiState.collectAsState()

    DisciplineEditScreenContent(
        uiState
    )
}

@Composable
fun DisciplineEditScreenContent(
    uiState : State<DisciplineEditUiState>
) {
    if (uiState.value.loading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircleLoading()
        }
    } else {
        LazyColumn {
            items(uiState.value.disciplines ?: listOf()) { discipline ->
                DisciplineCard(
                    discipline = discipline,
                    onNameChange = {
                        discipline.name = it
                    },
                    onSave = {

                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun DisciplineCard(
    discipline: Discipline,
    onNameChange : (String) -> Unit,
    onSave: suspend (Discipline) -> Unit
) {
    var isEditMode by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

//    LaunchedEffect(isEditMode) {
//        if (!isEditMode) {
//            onSave(Discipline(id = discipline.id, name = discipline.name))
//        }
//    }

    OutlinedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
            .clickable {
                isEditMode = !isEditMode
            }
    ) {
        AnimatedVisibility(
            visible = !isEditMode,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Text(
                text = discipline.name,
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
                Text("Редактирование записи #${discipline.id}")
                OutlinedTextField(
                    value = discipline.name,
                    onValueChange = onNameChange,
                    label = { Text("Наименование дисциплины") },
                    singleLine = true,
                    keyboardActions = KeyboardActions(onDone = {
                        isEditMode = false
                    }),
                    modifier = Modifier.focusRequester(focusRequester)
                )
            }
        }
    }
}