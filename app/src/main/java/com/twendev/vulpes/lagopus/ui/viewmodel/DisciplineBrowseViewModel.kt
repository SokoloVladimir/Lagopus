package com.twendev.vulpes.lagopus.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import com.twendev.vulpes.lagopus.model.Discipline
import com.twendev.vulpes.lagopus.ui.repository.DisciplineRepository


class DisciplineBrowseViewModel : LoadableViewModel() {
    private val repository = DisciplineRepository()

    val items = mutableStateListOf<Discipline>()
    private val itemsTrash = mutableListOf<Discipline>()

    init {
        refresh()
    }

    fun updateItem(item: Discipline) {
        if (isDublicate(item)) {
            refresh()
            return
        }

        suspendAction {
            repository.update(item)
        }
    }

    fun deleteItem(item: Discipline) {
        suspendAction {
            repository.delete(item)
        }
    }

    fun createItem(item: Discipline) {
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

    fun prepareDeleteItem(item: Discipline) {
        itemsTrash.add(item)
        items.remove(item)
    }

    fun confirmDeleteItem(item: Discipline) {
        if (itemsTrash.indexOf(item) == -1) {
            return
        }

        suspendAction {
            repository.delete(item)
            itemsTrash.remove(item)
        }
    }

    fun cancelDeleteItem(item: Discipline) {
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

    private fun isDublicate(item: Discipline) : Boolean {
        return items.any { it.name == item.name && it.id != item.id }
    }
}