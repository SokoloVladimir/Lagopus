package com.twendev.vulpes.lagopus.ui.component.searchabledropdown

data class SearchableDropdownUiState<T>(
    val isExpanded: Boolean = false,
    val selectedText: String = "",
    val selectedItem: T? = null
)