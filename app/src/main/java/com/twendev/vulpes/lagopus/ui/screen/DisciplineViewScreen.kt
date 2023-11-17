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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.twendev.vulpes.lagopus.ZerdaService
import com.twendev.vulpes.lagopus.model.Discipline
import com.twendev.vulpes.lagopus.ui.component.circleloading.CircleLoading

@Composable
fun DisciplineViewScreen(padding: PaddingValues) {
    Log.d("DisciplineViewScreen",  "Opened")

    val zerda = ZerdaService.Singleton!!
    var isLoaded by remember { mutableStateOf(false) }
    val disciplines : MutableList<Discipline> = remember { mutableListOf() }

    LaunchedEffect(isLoaded) {
        disciplines.addAll(zerda.api.getDisciplines().toList())
        isLoaded = true
    }

    if (isLoaded) {
        LazyColumn {
            items(disciplines.toList()) { discipline ->
                DisciplineCard(
                    discipline = discipline,
                    onSave = {
                        zerda.api.putDiscipline(it)
                    }
                )
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircleLoading()
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun DisciplineCard(discipline: Discipline, onSave: suspend (Discipline) -> Unit) {
    var isEditMode by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    var dId by remember { mutableIntStateOf(discipline.id) }
    var dName by remember { mutableStateOf(discipline.name) }

    LaunchedEffect(isEditMode) {
        if (!isEditMode) {
            onSave(Discipline(id = dId, name = dName))
        }
    }

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
                text = dName,
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
                Text("Редактирование записи #${dId}")
                OutlinedTextField(
                    value = dName,
                    onValueChange = { dName = it },
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