package com.twendev.vulpes.lagopus.ui.repository

import com.twendev.vulpes.lagopus.datasource.ZerdaService
import com.twendev.vulpes.lagopus.model.WorkType

class WorkTypeRepository : RepositoryInterface<WorkType> {
    private val zerdaSource = ZerdaService.Singleton

    override suspend fun get(id: Int) : WorkType? {
        return zerdaSource.api.getWorkTypes().firstOrNull { it.id == id}
    }
    override suspend fun get(): List<WorkType> {
        return zerdaSource.api.getWorkTypes().toList()
    }
    override suspend fun update(obj: WorkType) {
        return zerdaSource.api.putWorkType(obj)
    }
    override suspend fun create(obj: WorkType) : WorkType {
        return zerdaSource.api.postWorkType(obj)
    }
    override suspend fun delete(obj: WorkType) {
        zerdaSource.api.deleteWorkType(obj.id)
    }
}