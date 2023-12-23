package com.twendev.vulpes.lagopus.ui.viewmodel

import com.twendev.vulpes.lagopus.model.Discipline
import com.twendev.vulpes.lagopus.model.Semester
import com.twendev.vulpes.lagopus.model.Work
import com.twendev.vulpes.lagopus.model.WorkType
import com.twendev.vulpes.lagopus.ui.repository.Repositories
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class WorkAlterUiState(
    val item: Work = Work(),
    val disciplines: List<Discipline> = listOf(),
    val workTypes: List<WorkType> = listOf(),
    val semesters: List<Semester> = listOf()
)

class WorkAlterViewModel(private val itemId: Int) : LoadableViewModel() {
    private val repository = Repositories.work

    private val _uiState = MutableStateFlow(WorkAlterUiState())
    val uiState: StateFlow<WorkAlterUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun saveItem() {
        if (_uiState.value.item.id == 0) {
            suspendActionWithLoading {
                repository.create(_uiState.value.item)
            }
        } else {
            suspendActionWithLoading {
                repository.update(_uiState.value.item)
            }
        }
    }

    fun deleteItem() {
        suspendActionWithLoading {
            repository.delete(_uiState.value.item)
        }
    }

    fun updateItem(item: Work) {
        _uiState.update {
            it.copy(
                item = item
            )
        }
    }

    private fun refresh() {
        suspendActionWithLoading {
            _uiState.update {
                it.copy(
                    item = repository.get(itemId) ?: Work(),
                    disciplines = Repositories.discipline.get(),
                    workTypes = Repositories.workType.get(),
                    semesters =  Repositories.semester.get()
                )
            }
        }
    }
}