package com.twendev.vulpes.lagopus.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import com.twendev.vulpes.lagopus.model.Group
import com.twendev.vulpes.lagopus.model.Result
import com.twendev.vulpes.lagopus.model.Work
import com.twendev.vulpes.lagopus.ui.repository.Repositories
import com.twendev.vulpes.lagopus.ui.repository.ResultRepository


class ResultBrowseViewModel(
    private val groupId: Int,
    private val workId: Int
) : LoadableViewModel() {
    private val repository = ResultRepository()

    val items = mutableStateListOf<Result>()
    var work : Work? = null
    var group : Group? = null

    init {
        suspendAction {
            work = Repositories.work.get(workId)
            group = Repositories.group.get(groupId)
        }
        suspendActionWithLoading {
            refreshAsync()
            for (student in Repositories.student.getByGroupId(groupId = groupId)) {
                if (items.all { it.studentId != student.id }) {
                    items.add(Result(
                        studentId = student.id,
                        workId = workId,
                        tasks = 0u
                    ))
                }
            }

            for (item in items) {
                item.cacheStudent()
            }

            items.sortWith(compareBy<Result> { it.cachedStudent?.surname }.thenBy { it.cachedStudent?.name })
        }
    }

    private fun refresh() {
        suspendActionWithLoading {
            refreshAsync()
        }
    }

    private suspend fun refreshAsync() {
        items.clear()
        items.addAll(repository.get(groupId = groupId, workId = workId).toMutableList())
    }

    fun updateItem(item: Result) {
        suspendAction {
            repository.update(item)
        }
    }

    fun createItem(item: Result) {
        items.add(item)
    }

    private fun sort() {
        items.sortBy { it.studentId }
    }
}