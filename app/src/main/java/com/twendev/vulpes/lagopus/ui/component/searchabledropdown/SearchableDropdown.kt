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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import com.twendev.vulpes.lagopus.AppAddon


@Composable
fun <T> SearchableDropdown(
    placeholder : String,
    controller: SearchableDropdownController<T>
) {
    val uiState by controller.uiState.collectAsState();

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    SearchableDropdownContent(
        placeholder = placeholder,
        isExpanded = uiState.isExpanded,
        value = uiState.selectedText,
        filteredItems = uiState.filteredList,
        onExpandedChange = {
            controller.SwitchExpand()
        },
        onSelectionChanged = { selection : String ->
            if (controller.ChangeSelection(selection)) {
                AppAddon.ToastIsDebug("Selected $selection", context)
                focusManager.clearFocus()
            }
        },
        onValueChanged = {
            controller.ChangeSearch(it)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SearchableDropdownContent(
    placeholder: String,
    isExpanded: Boolean,
    value: String,
    filteredItems: List<T>,
    onExpandedChange : () -> Unit,
    onSelectionChanged : (selection: String) -> Unit,
    onValueChanged : (String) -> Unit
) {
    Box(
    ) {
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { onExpandedChange() }
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChanged,
                label = { Text(text = placeholder) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                modifier = Modifier.menuAnchor(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    onSelectionChanged(value)
                })
            )

            if (filteredItems.isNotEmpty()) {
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = {
                    }
                ) {
                    filteredItems.forEach { item ->
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