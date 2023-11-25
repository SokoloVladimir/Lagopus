package com.twendev.vulpes.lagopus.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import com.twendev.vulpes.lagopus.model.WorkType
import com.twendev.vulpes.lagopus.ui.repository.WorkTypeRepository


class WorkTypeBrowseViewModel : LoadableViewModel() {
    private val repository = WorkTypeRepository()

    val items = mutableStateListOf<WorkType>()
    private val itemsTrash = mutableListOf<WorkType>()

    init {
        refresh()
    }

    fun updateItem(item: WorkType) {
        if (isDublicate(item)) {
            refresh()
            return
        }

        suspendAction {
            repository.updateAndPush(item)
        }
    }

    fun deleteItem(item: WorkType) {
        suspendAction {
            repository.deleteAndPush(item)
        }
    }

    fun createItem(item: WorkType) {
        if (isDublicate(item)) {
            return
        }

        suspendActionWithLoading {
            val createdItem = repository.createAndPush(item)
            items.add(createdItem)
        }
    }

    private fun refresh() {
        suspendActionWithLoading {
            items.clear()
            items.addAll(repository.pullAndGet().toMutableList())
        }
    }

    fun prepareDeleteItem(item: WorkType) {
        itemsTrash.add(item)
        items.remove(item)
    }

    fun confirmDeleteItem(item: WorkType) {
        if (itemsTrash.indexOf(item) == -1) {
            return
        }

        suspendAction {
            repository.deleteAndPush(item)
            itemsTrash.remove(item)
        }
    }

    fun cancelDeleteItem(item: WorkType) {
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

    private fun isDublicate(item: WorkType) : Boolean {
        return items.any { it.name == item.name && it.id != item.id }
    }
}