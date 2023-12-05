package com.twendev.vulpes.lagopus.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import com.twendev.vulpes.lagopus.model.Group
import com.twendev.vulpes.lagopus.ui.repository.GroupRepository


class GroupBrowseViewModel : LoadableViewModel() {
    private val repository = GroupRepository()

    val items = mutableStateListOf<Group>()
    private val itemsTrash = mutableListOf<Group>()

    init {
        refresh()
    }

    fun updateItem(item: Group) {
        if (isDublicate(item)) {
            refresh()
            return
        }

        suspendAction {
            repository.update(item)
        }
    }

    fun deleteItem(item: Group) {
        suspendAction {
            repository.delete(item)
        }
    }

    fun createItem(item: Group) {
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

    fun prepareDeleteItem(item: Group) {
        itemsTrash.add(item)
        items.remove(item)
    }

    fun confirmDeleteItem(item: Group) {
        if (itemsTrash.indexOf(item) == -1) {
            return
        }

        suspendAction {
            repository.delete(item)
            itemsTrash.remove(item)
        }
    }

    fun cancelDeleteItem(item: Group) {
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

    private fun isDublicate(item: Group) : Boolean {
        return items.any { it.name == item.name && it.id != item.id }
    }
}