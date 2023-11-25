package com.twendev.vulpes.lagopus.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import com.twendev.vulpes.lagopus.model.Discipline
import com.twendev.vulpes.lagopus.model.Work
import com.twendev.vulpes.lagopus.model.WorkType
import com.twendev.vulpes.lagopus.ui.repository.Repositories
import com.twendev.vulpes.lagopus.ui.repository.WorkRepository

class WorkBrowseViewModel : LoadableViewModel() {
    private val repository = WorkRepository()

    val items = mutableStateListOf<Work>()
    var disciplines = listOf<Discipline>()
    var workTypes = listOf<WorkType>()

    private val itemsTrash = mutableListOf<Work>()

    init {
        refresh()
    }

    private fun refresh() {
        suspendActionWithLoading {
            items.clear()
            disciplines = Repositories.discipline.pullAndGet()
            workTypes = Repositories.workType.pullAndGet()
            items.addAll(repository.pullAndGet().toMutableList())
        }
    }

    fun updateItem(item: Work) {
        suspendAction {
            repository.updateAndPush(item)
        }
    }

    fun deleteItem(item: Work) {
        suspendAction {
            repository.deleteAndPush(item)
        }
    }

    fun createItem(item: Work) {
        suspendActionWithLoading {
            val createdItem = repository.createAndPush(item)
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
            repository.deleteAndPush(item)
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