package com.twendev.vulpes.lagopus.ui.screen.browse

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.twendev.vulpes.lagopus.model.Work
import com.twendev.vulpes.lagopus.ui.component.circleloading.CircleLoading
import com.twendev.vulpes.lagopus.ui.component.searchabledropdown.SearchableDropdown
import com.twendev.vulpes.lagopus.ui.component.searchabledropdown.SearchableDropdownController
import com.twendev.vulpes.lagopus.ui.viewmodel.LoadingStatus
import com.twendev.vulpes.lagopus.ui.viewmodel.LoadingUiState
import com.twendev.vulpes.lagopus.ui.viewmodel.WorkBrowseViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun WorkBrowseScreen(padding: PaddingValues, snackBarHostState: SnackbarHostState) {
    Log.d("WorkBrowseScreen",  "Opened")

    val viewModel by remember { mutableStateOf(WorkBrowseViewModel()) }
    val uiState = viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    WorkBrowseScreenContent(
        uiState = uiState,
        viewModel = viewModel,
        snackBarHostState = snackBarHostState,
        coroutineScope = scope
    )
}

@Composable
fun WorkBrowseScreenContent(
    uiState : State<LoadingUiState>,
    viewModel: WorkBrowseViewModel,
    snackBarHostState : SnackbarHostState,
    coroutineScope : CoroutineScope
) {
    Box {
        if (uiState.value.loading != LoadingStatus.None) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircleLoading()
            }
        }

        if (uiState.value.loading != LoadingStatus.Full) {
            LazyColumn(
                contentPadding = PaddingValues(15.dp)
            ) {
                item {
                    val controller = SearchableDropdownController(list = viewModel.disciplines)
                    SearchableDropdown(placeholder = "Дисциплина", controller = controller)

                    controller.onSelect = {

                    }
                    Spacer(Modifier.height(20.dp))
                }

                items(viewModel.items) { item ->
                    WorkCard(
                        item = item
                    )
                    Spacer(Modifier.height(15.dp))
                }

                item {
                    IconButton(
                        onClick = {
                            viewModel.createItem(Work())
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
    item : Work
) {
    WorkCardContent(
        item = item,
    )
}

@Composable
fun WorkCardContent(
    item: Work
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