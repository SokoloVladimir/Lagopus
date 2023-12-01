package com.twendev.vulpes.lagopus.ui.repository

import com.twendev.vulpes.lagopus.datasource.ZerdaService
import com.twendev.vulpes.lagopus.model.Semester

class SemesterRepository : RepositoryInterface<Semester> {
    private val zerdaSource = ZerdaService.Singleton
    suspend fun get(id: Int) : Semester? {
        return zerdaSource.api.getSemesters().firstOrNull { it.id == id}
    }
    override suspend fun get(): List<Semester> {
        return zerdaSource.api.getSemesters().toList()
    }
    override suspend fun update(obj: Semester) {
        return zerdaSource.api.putSemester(obj)
    }
    override suspend fun create(obj: Semester) : Semester {
        return zerdaSource.api.postSemester(obj)
    }
    override suspend fun delete(obj: Semester) {
        zerdaSource.api.deleteSemester(obj.id)
    }
}