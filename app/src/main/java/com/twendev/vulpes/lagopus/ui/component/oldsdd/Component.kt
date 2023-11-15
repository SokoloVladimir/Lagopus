package com.twendev.vulpes.lagopus.ui.component.oldsdd

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import com.twendev.vulpes.lagopus.AppAddon
import com.twendev.vulpes.lagopus.model.Work


@Composable
fun <T> SearchableDropdown(
    placeholderText : String,
    itemList: Array<T>
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    SearchableDropdownContent(
        placeholder = placeholderText,
        isExpanded = expanded,
        value = selectedText,
        items = itemList.toList(),
        onExpandedChange = {
            expanded = !expanded;
        },
        onSelectionChanged = { selection : String ->
            selectedText = selection
            AppAddon.ToastIsDebug("Группа: $selection", context)

            expanded = false
            focusManager.clearFocus()
        },
        onValueChanged = { value: String ->
            selectedText = value
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SearchableDropdownContent(
    placeholder: String,
    isExpanded: Boolean,
    value: String,
    items: List<T>,
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
                    if (items.any { it.toString().equals(value, ignoreCase = true) } ) {
                        onSelectionChanged(value)
                    }
                })
            )

            val filteredItems = if (items.firstOrNull() is Work) {
                items.filter { (it as Work).isSimular(value) };
            } else {
                items.filter { it.toString().contains(value, ignoreCase = true) };
            }

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
