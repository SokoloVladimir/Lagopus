package com.twendev.vulpes.lagopus.page

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.*
import com.twendev.vulpes.lagopus.AppAddon

@Composable
fun MainPage(padding: PaddingValues, showSnackBar: (text: String) -> Unit)
{
    val groups = arrayOf("ИСПП-01", "ИСПП-11", "ССА-01", "ССА-15", "Р-22", "Р-20", "ССО-21", "ССО-20", "ССО-15")

    SearchableExposedDropdownMenuBox(groups);
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchableExposedDropdownMenuBox(
    itemList: Array<String>
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val changeSelectionTo = { selection : String ->
        selectedText = selection
        AppAddon.ToastIsDebug("Группа: $selection", context)

        expanded = false
        focusManager.clearFocus()
    }

    Box(
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            OutlinedTextField(
                value = selectedText,
                onValueChange = { selectedText = it },
                label = { Text(text = "Группа...") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(),
                singleLine = true,
                keyboardActions = KeyboardActions(onDone = {
                    if (itemList.any { it.contains(selectedText, ignoreCase = true) } ) {
                        changeSelectionTo(selectedText)
                    }
                })
            )

            val filteredOptions =
                itemList.filter { it.contains(selectedText, ignoreCase = true) }
            if (filteredOptions.isNotEmpty()) {
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                    }
                ) {
                    filteredOptions.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                changeSelectionTo(item)
                            }
                        )
                    }
                }
            }
        }
    }
}
