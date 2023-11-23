package com.twendev.vulpes.lagopus.ui.repository

import com.twendev.vulpes.lagopus.ZerdaService
import com.twendev.vulpes.lagopus.model.Discipline

class DisciplineEditRepository {
    private var cache : List<Discipline> = listOf()
    private suspend fun update() {
        cache = ZerdaService.Singleton!!.api.getDisciplines().toList()
    }
    fun get(): List<Discipline> {
        return cache
    }

    suspend fun updateAndGet(): List<Discipline> {
        update()
        return get()
    }
}