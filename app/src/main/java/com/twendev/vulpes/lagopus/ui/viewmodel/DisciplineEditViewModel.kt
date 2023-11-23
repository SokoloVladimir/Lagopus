package com.twendev.vulpes.lagopus.ui.viewmodel

import android.util.Log
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
    private val disciplinesTrash = mutableListOf<Discipline>()

    fun updateDiscipline(discipline: Discipline) {
        if (isDublicate(discipline)) {
            refresh()
            return
        }

        suspendAction {
            repository.updateAndPush(discipline)
        }
    }

    fun prepareDeleteDiscipline(discipline: Discipline) {
        Log.d("DisciplineViewModel","prepareDelete $discipline")
        disciplinesTrash.add(discipline)
        disciplines.remove(discipline)
    }

    fun confirmDeleteDiscipline(discipline: Discipline) {
        Log.d("DisciplineViewModel","confirmDelete $discipline")
        if (disciplinesTrash.indexOf(discipline) == -1) {
            return
        }

        suspendAction {
            repository.deleteAndPush(discipline)
            disciplinesTrash.remove(discipline)
        }
    }

    fun cancelDeleteDiscipline(discipline: Discipline) {
        Log.d("DisciplineViewModel","cancelDelete $discipline")
        if (disciplinesTrash.indexOf(discipline) == -1) {
            return
        }

        suspendAction {
            disciplinesTrash.remove(discipline)
            disciplines.add(discipline)
            sort()
        }
    }

    fun createDiscipline(discipline: Discipline) {
        if (isDublicate(discipline)) {
            return
        }

        suspendActionWithLoading {
            val createdDiscipline = repository.createAndPush(discipline)
            disciplines.add(createdDiscipline)
        }
    }

    init {
        refresh()
    }

    private fun refresh() {
        suspendActionWithLoading {
            disciplines.clear()
            disciplines.addAll(repository.pullAndGet().toMutableList())
        }
    }

    private fun sort() {
        disciplines.sortBy { it.id }
    }

    private fun suspendActionWithLoading(action: suspend () -> Unit) {
        setLoadingState(true)
        viewModelScope.launch {
            action()
            setLoadingState(false)
        }
    }
    private fun suspendAction(action: suspend () -> Unit) {
        viewModelScope.launch {
            action()
        }
    }
    private fun setLoadingState(state: Boolean) {
        _uiState.update { it.copy( loading = state) }
    }
    private fun isDublicate(discipline: Discipline) : Boolean {
        return disciplines.any { it.name == discipline.name && it.id != discipline.id }
    }
}