package com.twendev.vulpes.lagopus.ui.pages

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.twendev.vulpes.lagopus.model.Work
import com.twendev.vulpes.lagopus.model.WorkType
import com.twendev.vulpes.lagopus.ui.component.searchabledropdown.SearchableDropdown
import com.twendev.vulpes.lagopus.ui.component.searchabledropdown.SearchableDropdownController

@Composable
fun MainPage(padding: PaddingValues, showSnackBar: (text: String) -> Unit)
{
    val groups = arrayOf("ИСПП-01", "ИСПП-11", "ССА-01", "ССА-15", "Р-22", "Р-20", "ССО-21", "ССО-20", "ССО-15");
    val disciplines = arrayOf("MDK.01.02", "MDK.03.01", "MDK.01.01");
    val works = arrayOf(
        Work(1, WorkType.Static.Lab),
        Work(2, WorkType.Static.Lab),
        Work(3, WorkType.Static.Lab),
        Work(4, WorkType.Static.Lab),
        Work(1, WorkType.Static.Pract),
        Work(2, WorkType.Static.Pract),
        Work(3, WorkType.Static.Pract),
        Work(1, WorkType.Static.PractTask),
    );

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val controller = SearchableDropdownController(groups.toList());

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
}