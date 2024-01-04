package com.twendev.vulpes.lagopus.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import com.twendev.vulpes.lagopus.extensions.cacheWork
import com.twendev.vulpes.lagopus.model.Result
import com.twendev.vulpes.lagopus.ui.repository.ResultRepository

class StudentResultViewModel(

) : LoadableViewModel() {
    private val repository = ResultRepository()

    val items = mutableStateListOf<Result>()

    init {
        suspendActionWithLoading {
            refreshAsync()

            for (item in items) {
                item.cacheWork()
            }
        }
    }

    private fun refresh() {
        suspendActionWithLoading {
            refreshAsync()
        }
    }

    private suspend fun refreshAsync() {
        items.clear()
        items.addAll(repository.get().toMutableList())
    }
}