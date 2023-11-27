package com.twendev.vulpes.lagopus.ui.screen.browse

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.twendev.vulpes.lagopus.model.Work
import com.twendev.vulpes.lagopus.ui.component.circleloading.CircleLoading
import com.twendev.vulpes.lagopus.ui.component.searchabledropdown.SearchableDropdown
import com.twendev.vulpes.lagopus.ui.component.searchabledropdown.SearchableDropdownController
import com.twendev.vulpes.lagopus.ui.screen.Screen
import com.twendev.vulpes.lagopus.ui.viewmodel.LoadingStatus
import com.twendev.vulpes.lagopus.ui.viewmodel.LoadingUiState
import com.twendev.vulpes.lagopus.ui.viewmodel.WorkBrowseUiState
import com.twendev.vulpes.lagopus.ui.viewmodel.WorkBrowseViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun WorkBrowseScreen(padding: PaddingValues, snackBarHostState: SnackbarHostState, navController : NavController) {
    Log.d("WorkBrowseScreen",  "Opened")

    val viewModel by remember { mutableStateOf(WorkBrowseViewModel()) }
    val loadingUiState = viewModel.loadingUiState.collectAsState()
    val uiState = viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    WorkBrowseScreenContent(
        loadingUiState = loadingUiState.value,
        uiState = uiState.value,
        viewModel = viewModel,
        snackBarHostState = snackBarHostState,
        coroutineScope = scope,
        navToWorkById = {
            navController.navigate(Screen.WorkAlterScreen.createWithId(it))
        }
    )
}

@Composable
fun WorkBrowseScreenContent(
    loadingUiState : LoadingUiState,
    uiState: WorkBrowseUiState,
    viewModel: WorkBrowseViewModel,
    snackBarHostState : SnackbarHostState,
    coroutineScope : CoroutineScope,
    navToWorkById: (Int) -> Unit
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
            // TODO: нарушение stateless
            val disciplineController by remember { mutableStateOf(SearchableDropdownController(list = uiState.disciplines)) }
            val workTypeController by remember { mutableStateOf(SearchableDropdownController(list = uiState.workTypes)) }

            disciplineController.onSelect = { viewModel.filterByDiscipline(it) }
            disciplineController.onSearchChange = { viewModel.filterByDiscipline(null) }

            workTypeController.onSelect = { viewModel.filterByWorkType(it) }
            workTypeController.onSearchChange = { viewModel.filterByWorkType(null) }

            var isFilterExpanded by remember { mutableStateOf(false) }
            Column {
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
                            SearchableDropdown(placeholder = "Дисциплина", controller = disciplineController)
                            SearchableDropdown(placeholder = "Тип работы", controller = workTypeController)
                        }
                    }
                }

                Divider(color = Color.Black, thickness = 1.dp)

                LazyColumn(
                    contentPadding = PaddingValues(15.dp)
                ) {

                    items(viewModel.items) { item ->
                        if (
                            !isFilterExpanded ||
                            (uiState.selectedDiscipline == null || uiState.selectedDiscipline == item.discipline)
                            && (uiState.selectedWorkType == null || uiState.selectedWorkType == item.workType)
                        ) {
                            WorkCard(
                                item = item,
                                onClick = {
                                    navToWorkById(it)
                                }
                            )
                            Spacer(Modifier.height(15.dp))
                        }
                    }

                    item {
                        IconButton(
                            onClick = {
                                navToWorkById(0)
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
}

@Composable
fun WorkCard(
    item : Work,
    onClick: (Int) -> Unit
) {
    WorkCardContent(
        item = item,
        onClick = onClick
    )
}

@Composable
fun WorkCardContent(
    item: Work,
    onClick : (Int) -> Unit
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
                onClick(item.id)
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