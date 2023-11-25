package com.twendev.vulpes.lagopus.ui.repository

import com.twendev.vulpes.lagopus.datasource.ZerdaService
import com.twendev.vulpes.lagopus.model.WorkType

class WorkTypeRepository : RepositoryInterface<WorkType> {
    private val zerdaSource = ZerdaService.Singleton

    override suspend fun pullAndGet(): List<WorkType> {
        return zerdaSource.api.getWorkTypes().toList()
    }
    override suspend fun updateAndPush(obj: WorkType) {
        return zerdaSource.api.putWorkType(obj)
    }
    override suspend fun createAndPush(obj: WorkType) : WorkType {
        return zerdaSource.api.postWorkType(obj)
    }
    override suspend fun deleteAndPush(obj: WorkType) {
        zerdaSource.api.deleteWorkType(obj.id)
    }
}