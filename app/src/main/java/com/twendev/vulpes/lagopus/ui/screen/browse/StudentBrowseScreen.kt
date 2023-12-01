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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.twendev.vulpes.lagopus.model.Student
import com.twendev.vulpes.lagopus.ui.component.circleloading.CircleLoading
import com.twendev.vulpes.lagopus.ui.viewmodel.LoadingStatus
import com.twendev.vulpes.lagopus.ui.viewmodel.LoadingUiState
import com.twendev.vulpes.lagopus.ui.viewmodel.StudentBrowseViewModel

@Composable
fun StudentBrowseScreen(groupId: Int, onItemClick: (Int) -> Unit) {
    Log.d("StudentBrowseScreen",  "Opened")

    val viewModel by remember { mutableStateOf(StudentBrowseViewModel(groupId)) }
    val loadingUiState = viewModel.loadingUiState.collectAsState()

    StudentBrowseScreenContent(
        loadingUiState = loadingUiState.value,
        viewModel = viewModel,
        onItemClick = onItemClick,
    )
}

@Composable
fun StudentBrowseScreenContent(
    loadingUiState : LoadingUiState,
    viewModel: StudentBrowseViewModel,
    onItemClick: (Int) -> Unit
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
                        StudentCard(
                            item = item,
                            onClick = {
                                onItemClick(it)
                            }
                        )
                        Spacer(Modifier.height(15.dp))
                    }

                    item {
                        IconButton(
                            onClick = {
                                onItemClick(0)
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
fun StudentCard(
    item : Student,
    onClick: (Int) -> Unit
) {
    StudentCardContent(
        item = item,
        onClick = onClick
    )
}

@Composable
fun StudentCardContent(
    item: Student,
    onClick : (Int) -> Unit
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