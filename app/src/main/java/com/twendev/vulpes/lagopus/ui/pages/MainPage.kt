package com.twendev.vulpes.lagopus.ui.pages

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.twendev.vulpes.lagopus.ZerdaService
import com.twendev.vulpes.lagopus.model.Work
import com.twendev.vulpes.lagopus.ui.component.searchabledropdown.SearchableDropdown
import com.twendev.vulpes.lagopus.ui.component.searchabledropdown.SearchableDropdownController

@Composable
fun MainPage(padding: PaddingValues, showSnackBar: (text: String) -> Unit)
{
    val zerda = ZerdaService()
    var works by remember { mutableStateOf<List<Work>>(listOf()) }

    LaunchedEffect(works) {
        works = zerda.GetWorks().toList()
        Log.d("MainPage",works.firstOrNull()?.theme ?: "")
    }


    if (works.isNotEmpty()) {
        val controller = SearchableDropdownController(works)

        LazyColumn {
            item {
                SearchableDropdown(
                    placeholder = "Группа",
                    controller = controller
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = {
                    showSnackBar(controller.uiState.value.selectedText)
                }) {
                    Text(text = "snack")
                }
            }
        }
    } else {
        Text("loading...")
    }

}