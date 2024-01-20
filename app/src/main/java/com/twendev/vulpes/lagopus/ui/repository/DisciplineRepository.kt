package com.twendev.vulpes.lagopus.ui.repository

import com.twendev.vulpes.lagopus.datasource.ZerdaService
import com.twendev.vulpes.lagopus.model.Discipline
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DisciplineRepository : RepositoryInterface<Discipline>, KoinComponent {
    private val zerdaSource : ZerdaService by inject()
    suspend fun get(id: Int) : Discipline? {
        return zerdaSource.api.getDisciplines().firstOrNull { it.id == id}
    }
    override suspend fun get(): List<Discipline> {
        return zerdaSource.api.getDisciplines().toList()
    }
    override suspend fun update(obj: Discipline) {
        return zerdaSource.api.putDiscipline(obj)
    }
    override suspend fun create(obj: Discipline) : Discipline {
        return zerdaSource.api.postDiscipline(obj)
    }
    override suspend fun delete(obj: Discipline) {
        zerdaSource.api.deleteDiscipline(obj.id)
    }
}