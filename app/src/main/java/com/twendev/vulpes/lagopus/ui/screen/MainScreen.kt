package com.twendev.vulpes.lagopus.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.twendev.vulpes.lagopus.ZerdaService
import com.twendev.vulpes.lagopus.model.User
import com.twendev.vulpes.lagopus.model.Work
import com.twendev.vulpes.lagopus.ui.component.circleloading.CircleLoading
import com.twendev.vulpes.lagopus.ui.component.searchabledropdown.SearchableDropdown
import com.twendev.vulpes.lagopus.ui.component.searchabledropdown.SearchableDropdownController

@Composable
fun MainScreen(padding: PaddingValues, instanceUrl: String? = null)
{
    Log.d("MainScreen", "started with $instanceUrl")

    var loadingStatus by remember { mutableStateOf(false) }
    val zerda = ZerdaService(if (instanceUrl.isNullOrBlank()) null else instanceUrl)
    var works by remember { mutableStateOf<List<Work>>(listOf()) }
    var users by remember { mutableStateOf<List<User>>(listOf()) }

    LaunchedEffect(loadingStatus) {
        works = zerda.api.getWorks().toList()
        users = zerda.api.getUsers().toList()

        loadingStatus = true
    }

    if (loadingStatus) {
        val workController = SearchableDropdownController(works) { }
        val userController = SearchableDropdownController(users) { }

        LazyColumn {
            item {
                SearchableDropdown(
                    placeholder = "Work",
                    controller = workController
                )
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                SearchableDropdown(
                    placeholder = "User",
                    controller = userController
                )
                Spacer(modifier = Modifier.height(20.dp))
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