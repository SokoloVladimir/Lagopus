package com.twendev.vulpes.lagopus.ui.component.searchabledropdown

import androidx.lifecycle.ViewModel
import com.twendev.vulpes.lagopus.model.Work
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SearchableDropdownViewModel<T>(list: List<T>) : ViewModel() {
    private val _uiState : MutableStateFlow<SearchableDropdownUiState<T>>
    val uiState: StateFlow<SearchableDropdownUiState<T>>

    init {
        _uiState = MutableStateFlow(SearchableDropdownUiState(dropdownList = list))
        uiState = _uiState.asStateFlow();
    }

    fun SwitchExpand(state: Boolean? = null) {
        _uiState.update {
            it.copy(isExpanded = state ?: !it.isExpanded)
        }

        if (_uiState.value.isExpanded) {
            Refilter();
        }
    }

    fun ChangeSearch(value: String) {
        _uiState.update {
            it.copy(selectedText = value)
        }

        Refilter(value)
    }

    /**
     * Функция выбора из списка
     * @return Boolean если выбор успешен
     */
    fun ChangeSelection(value: String) : Boolean {
        _uiState.value.dropdownList.forEach { listElement ->
            if (listElement.toString().equals(value, ignoreCase = true)) {
                _uiState.update { state ->
                    state.copy(selectedText = listElement.toString(), isExpanded = false)
                    return true;
                }
            }
        }
        return false;
    }

    private fun Refilter(value: String? = null) {
        _uiState.update { state ->
            state.copy(
                filteredList = FilterList(state.dropdownList, value ?: state.selectedText),
            )
        }
    }

    private fun FilterList(listToFilter: List<T>, filterText: String) : List<T> {
        val filtered: MutableList<T> = (if (listToFilter.firstOrNull() is Work) {
            listToFilter.filter { (it as Work).isSimular(filterText) }
        } else {
            listOf()
        }).toMutableList()

        filtered += listToFilter.filter {
            it.toString().contains(filterText, ignoreCase = true)
        }
        return filtered.toList();
    }
}