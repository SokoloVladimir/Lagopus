package com.twendev.vulpes.lagopus.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import com.twendev.vulpes.lagopus.model.Discipline
import com.twendev.vulpes.lagopus.model.Semester
import com.twendev.vulpes.lagopus.model.Work
import com.twendev.vulpes.lagopus.model.WorkType
import com.twendev.vulpes.lagopus.ui.repository.Repositories
import com.twendev.vulpes.lagopus.ui.repository.WorkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class WorkBrowseUiState(
    val disciplines: List<Discipline> = listOf(),
    val workTypes: List<WorkType> = listOf(),
    val semesters: List<Semester> = listOf(),
    val selectedDiscipline: Discipline? = null,
    val selectedWorkType: WorkType? = null,
    val selectedSemester: Semester? = null
)

class WorkBrowseViewModel : LoadableViewModel() {
    private val repository = WorkRepository()

    private val _uiState = MutableStateFlow(WorkBrowseUiState())
    val uiState: StateFlow<WorkBrowseUiState> = _uiState.asStateFlow()

    val items = mutableStateListOf<Work>()
    private val itemsTrash = mutableListOf<Work>()

    init {
        refresh()
    }

    fun filterByDiscipline(discipline: Discipline?) {
        _uiState.update {
            it.copy(selectedDiscipline = discipline)
        }
    }

    fun filterByWorkType(workType: WorkType?) {
        _uiState.update {
            it.copy(selectedWorkType = workType)
        }
    }

    fun filterBySemester(semester: Semester?) {
        _uiState.update {
            it.copy(selectedSemester = semester)
        }
    }

    private fun refresh() {
        suspendActionWithLoading {
            items.clear()
            _uiState.update {
                it.copy(
                    disciplines = Repositories.discipline.get(),
                    workTypes = Repositories.workType.get(),
                    semesters = Repositories.semester.get()
                )
            }
            items.addAll(repository.get().toMutableList())
        }
    }

    fun updateItem(item: Work) {
        suspendAction {
            repository.update(item)
        }
    }

    fun deleteItem(item: Work) {
        suspendAction {
            repository.delete(item)
        }
    }

    fun createItem(item: Work) {
        suspendActionWithLoading {
            val createdItem = repository.create(item)
            items.add(createdItem)
        }
    }

    fun prepareDeleteItem(item: Work) {
        itemsTrash.add(item)
        items.remove(item)
    }

    fun confirmDeleteItem(item: Work) {
        if (itemsTrash.indexOf(item) == -1) {
            return
        }

        suspendAction {
            repository.delete(item)
            itemsTrash.remove(item)
        }
    }

    fun cancelDeleteItem(item: Work) {
        if (itemsTrash.indexOf(item) == -1) {
            return
        }

        suspendAction {
            itemsTrash.remove(item)
            items.add(item)
            sort()
        }
    }

    private fun sort() {
        items.sortBy { it.id }
    }
}