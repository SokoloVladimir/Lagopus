package com.twendev.vulpes.lagopus.ui.component.dropdown

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun <T> OutlinedDropdown(
    items: List<T>,
    onSelect: (T) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    preselectedItem: T? = null,
    displayItem: ((T?) -> String)? = null
) {
    var expandedState by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(preselectedItem) }
    val stringify : (T?) -> String = {
        if (it == null) {
            ""
        } else {
            displayItem?.invoke(it) ?: it.toString()
        }
    }

    OutlinedDropdownContent(
        items = items,
        selectedItem = selected,
        placeholder = placeholder,
        isExpanded = expandedState,
        setIsExpanded = { expandedState = it },
        itemToString = stringify,
        onItemSelect = {
            selected = it
            onSelect(it)
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> OutlinedDropdownContent(
    items: List<T>,
    selectedItem: T?,
    placeholder: String,
    isExpanded: Boolean,
    setIsExpanded: (Boolean) -> Unit,
    itemToString: (T?) -> String,
    onItemSelect: (T) -> Unit,
    modifier: Modifier
) {
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { setIsExpanded(!isExpanded) },
    ) {
        OutlinedTextField(
            value = itemToString(selectedItem),
            onValueChange = { },
            label = { Text(text = placeholder) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            singleLine = true,
            readOnly = true,
            modifier = modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { setIsExpanded(false) }
        ) {
            for (item in items) {
                DropdownMenuItem(
                    text = { Text(itemToString(item)) },
                    onClick = {
                        onItemSelect(item)
                        setIsExpanded(false)
                    }
                )
            }
        }
    }
}