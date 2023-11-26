package com.twendev.vulpes.lagopus.ui.component.searchabledropdown

import androidx.compose.runtime.toMutableStateList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SearchableDropdownController<T>(
    list: List<T>,
    var onSelect : (T) -> Unit = {},
    var onKeyboardAction : ((String) -> Unit)? = null,
    var onSearchChange: ((String) -> Unit)? = null
) {
    private val _uiState = MutableStateFlow(SearchableDropdownUiState<T>())
    val uiState = _uiState.asStateFlow()

    val items = list.toMutableStateList()

    fun switchExpand(state: Boolean? = null) {
        updateExpanded(state ?: !uiState.value.isExpanded)
    }

    fun changeSearch(value: String) {
        updateSearchText(value)
        resetSelectedItem()
        onSearchChange?.let { it(value) }
    }

    /**
     * Функция выбора из списка
     * @return Boolean если выбор успешен
     */
    fun changeSelection(value: String) : Boolean {
        for (item in items) {
            if (item.toString().equals(value, ignoreCase = true)) {
                updateSelectedItem(item)
                switchExpand()
                onSelect(item)
                return true
            }
        }
        return false
    }

    fun keyboardAction(value : String) {
        if (onKeyboardAction == null) {
            changeSelection(value)
        } else {
            onKeyboardAction?.let { it(value) }
        }

    }

    fun isFilter(item: T) : Boolean {
        return item.toString().contains(uiState.value.selectedText, ignoreCase = true)
    }
    private fun updateSearchText(value: String) {
        _uiState.update {
            it.copy(selectedText = value)
        }
    }

    private fun updateSelectedItem(item : T) {
        _uiState.update {
            it.copy(selectedItem = item, selectedText = item.toString())
        }
    }

    private fun resetSelectedItem() {
        _uiState.update {
            it.copy(selectedItem = null)
        }
    }

    private fun updateExpanded(state : Boolean) {
        _uiState.update {
            it.copy(isExpanded = state)
        }
    }
}