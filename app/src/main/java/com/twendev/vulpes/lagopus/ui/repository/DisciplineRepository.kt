package com.twendev.vulpes.lagopus.ui.repository

import com.twendev.vulpes.lagopus.datasource.ZerdaService
import com.twendev.vulpes.lagopus.model.Discipline

class DisciplineRepository : RepositoryInterface<Discipline> {
    private val zerdaSource = ZerdaService.Singleton

    override suspend fun pullAndGet(): List<Discipline> {
        return zerdaSource.api.getDisciplines().toList()
    }
    override suspend fun updateAndPush(obj: Discipline) {
        return zerdaSource.api.putDiscipline(obj)
    }
    override suspend fun createAndPush(obj: Discipline) : Discipline {
        return zerdaSource.api.postDiscipline(obj)
    }
    override suspend fun deleteAndPush(obj: Discipline) {
        zerdaSource.api.deleteDiscipline(obj.id)
    }
}