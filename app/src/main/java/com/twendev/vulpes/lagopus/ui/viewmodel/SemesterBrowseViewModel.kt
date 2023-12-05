package com.twendev.vulpes.lagopus.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import com.twendev.vulpes.lagopus.model.Semester
import com.twendev.vulpes.lagopus.ui.repository.SemesterRepository


class SemesterBrowseViewModel : LoadableViewModel() {
    private val repository = SemesterRepository()

    val items = mutableStateListOf<Semester>()
    private val itemsTrash = mutableListOf<Semester>()

    init {
        refresh()
    }

    fun updateItem(item: Semester) {
        if (isDublicate(item)) {
            refresh()
            return
        }

        suspendAction {
            repository.update(item)
        }
    }

    fun deleteItem(item: Semester) {
        suspendAction {
            repository.delete(item)
        }
    }

    fun createItem(item: Semester) {
        if (isDublicate(item)) {
            return
        }

        suspendActionWithLoading {
            val createdItem = repository.create(item)
            items.add(createdItem)
        }
    }

    private fun refresh() {
        suspendActionWithLoading {
            items.clear()
            items.addAll(repository.get().toMutableList())
        }
    }

    fun prepareDeleteItem(item: Semester) {
        itemsTrash.add(item)
        items.remove(item)
    }

    fun confirmDeleteItem(item: Semester) {
        if (itemsTrash.indexOf(item) == -1) {
            return
        }

        suspendAction {
            repository.delete(item)
            itemsTrash.remove(item)
        }
    }

    fun cancelDeleteItem(item: Semester) {
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

    private fun isDublicate(item: Semester) : Boolean {
        return items.any { it.startYear == item.startYear && it.isSecond == item.isSecond && it.id != item.id }
    }
}