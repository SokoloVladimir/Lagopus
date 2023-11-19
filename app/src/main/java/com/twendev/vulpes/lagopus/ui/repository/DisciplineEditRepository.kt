package com.twendev.vulpes.lagopus.ui.repository

import com.twendev.vulpes.lagopus.ZerdaService
import com.twendev.vulpes.lagopus.model.Discipline

class DisciplineEditRepository {
    suspend fun load(): List<Discipline> {
        return ZerdaService.Singleton!!.api.getDisciplines().toList()
    }
}