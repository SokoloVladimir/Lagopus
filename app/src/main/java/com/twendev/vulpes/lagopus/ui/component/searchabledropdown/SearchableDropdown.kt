package com.twendev.vulpes.lagopus.ui.component.searchabledropdown

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction


@Composable
fun <T> SearchableDropdown(
    placeholder : String,
    controller: SearchableDropdownController<T>
) {
    val uiState by controller.uiState.collectAsState()

    SearchableDropdownContent(
        placeholder = placeholder,
        uiState = uiState,
        items = controller.items,
        onExpandedChange = {
            controller.switchExpand()
        },
        onSelectionChanged = { selection : String ->
            controller.changeSelection(selection)
        },
        onKeyboardAction = { selection: String ->
            controller.keyboardAction(selection)
        },
        onValueChanged = {
            controller.changeSearch(it)
        },
        isFiltered = {
            controller.isFilter(it)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SearchableDropdownContent(
    placeholder: String,
    uiState: SearchableDropdownUiState<T>,
    items: List<T>,
    onExpandedChange : () -> Unit,
    onSelectionChanged : (selection: String) -> Unit,
    onKeyboardAction : (selection: String) -> Unit,
    onValueChanged : (String) -> Unit,
    isFiltered : (T) -> Boolean
) {
    Box(
    ) {
        ExposedDropdownMenuBox(
            expanded = uiState.isExpanded,
            onExpandedChange = { onExpandedChange() }
        ) {
            OutlinedTextField(
                value = uiState.selectedText,
                onValueChange = onValueChanged,
                label = { Text(text = placeholder) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.isExpanded) },
                modifier = Modifier.menuAnchor(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    onKeyboardAction(uiState.selectedText)
                })
            )

            ExposedDropdownMenu(
                expanded = uiState.isExpanded,
                onDismissRequest = {
                }
            ) {
                items.forEach { item ->
                    if (isFiltered(item) || uiState.selectedItem != null) {
                        DropdownMenuItem(
                            text = { Text(text = item.toString()) },
                            onClick = {
                                onSelectionChanged(item.toString())
                            }
                        )
                    }
                }
            }
        }
    }
}
