package com.twendev.vulpes.lagopus.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import com.twendev.vulpes.lagopus.model.Discipline
import com.twendev.vulpes.lagopus.model.Semester
import com.twendev.vulpes.lagopus.model.Student
import com.twendev.vulpes.lagopus.model.Work
import com.twendev.vulpes.lagopus.model.WorkType
import com.twendev.vulpes.lagopus.ui.repository.Repositories
import com.twendev.vulpes.lagopus.ui.repository.StudentRepository
import com.twendev.vulpes.lagopus.ui.repository.WorkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StudentBrowseViewModel(private val groupId: Int) : LoadableViewModel() {
    private val repository = StudentRepository()

    val items = mutableStateListOf<Student>()
    private val itemsTrash = mutableListOf<Student>()

    init {
        refresh()
    }

    private fun refresh() {
        suspendActionWithLoading {
            items.clear()
            items.addAll(repository.getByGroupId(groupId).toMutableList())
        }
    }

    fun updateItem(item: Student) {
        suspendAction {
            repository.update(item)
        }
    }

    fun deleteItem(item: Student) {
        suspendAction {
            repository.delete(item)
        }
    }

    fun createItem(item: Student) {
        suspendActionWithLoading {
            val createdItem = repository.create(item)
            items.add(createdItem)
        }
    }

    fun prepareDeleteItem(item: Student) {
        itemsTrash.add(item)
        items.remove(item)
    }

    fun confirmDeleteItem(item: Student) {
        if (itemsTrash.indexOf(item) == -1) {
            return
        }

        suspendAction {
            repository.delete(item)
            itemsTrash.remove(item)
        }
    }

    fun cancelDeleteItem(item: Student) {
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