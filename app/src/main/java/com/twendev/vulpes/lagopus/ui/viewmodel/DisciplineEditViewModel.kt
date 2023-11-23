package com.twendev.vulpes.lagopus.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twendev.vulpes.lagopus.model.Discipline
import com.twendev.vulpes.lagopus.ui.repository.DisciplineEditRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DisciplineEditUiState(
    val loading : Boolean = true
)

class DisciplineEditViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DisciplineEditUiState())
    val uiState: StateFlow<DisciplineEditUiState> = _uiState.asStateFlow()
    private val repository = DisciplineEditRepository()

    val disciplines = mutableStateListOf<Discipline>()



    init {
        viewModelScope.launch {
            disciplines.clear()
            disciplines.addAll(repository.updateAndGet().toMutableList())
            _uiState.update { it.copy( loading = false) }
        }
    }
}