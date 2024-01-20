package com.twendev.vulpes.lagopus.ui.viewmodel

import com.twendev.vulpes.lagopus.model.Assignment
import com.twendev.vulpes.lagopus.model.Group
import com.twendev.vulpes.lagopus.model.Work
import com.twendev.vulpes.lagopus.ui.repository.AssignmentRepository
import com.twendev.vulpes.lagopus.ui.repository.GroupRepository
import com.twendev.vulpes.lagopus.ui.repository.WorkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AssignmentAlterUiState(
    val item: Assignment = Assignment(),
    val work: Work = Work(),
    val group: Group = Group()
)

class AssignmentAlterViewModel(
    private val workId: Int,
    private val groupId: Int
) : LoadableViewModel() {
    private val repository = AssignmentRepository()

    private val _uiState = MutableStateFlow(AssignmentAlterUiState())
    val uiState: StateFlow<AssignmentAlterUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun saveItem() {
        suspendActionWithLoading {
            repository.update(_uiState.value.item)
        }
    }

    fun deleteItem() {
        suspendActionWithLoading {
            repository.delete(_uiState.value.item)
        }
    }

    fun updateItem(item: Assignment) {
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
                    item = repository.get(workId = workId, groupId = groupId) ?: Assignment(workId = workId, groupId = groupId),
                    work = WorkRepository().get(workId)!!,
                    group = GroupRepository().get(groupId)!!
                )
            }
        }
    }
}