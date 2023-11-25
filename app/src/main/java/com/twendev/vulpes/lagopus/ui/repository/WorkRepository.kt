package com.twendev.vulpes.lagopus.ui.repository

import com.twendev.vulpes.lagopus.datasource.ZerdaService
import com.twendev.vulpes.lagopus.model.Work

class WorkRepository : RepositoryInterface<Work> {
    private val zerdaSource = ZerdaService.Singleton

    override suspend fun pullAndGet(): List<Work> {
        return zerdaSource.api.getWorks().toList()
    }
    override suspend fun updateAndPush(obj: Work) {
        return zerdaSource.api.putWork(obj)
    }
    override suspend fun createAndPush(obj: Work) : Work {
        return zerdaSource.api.postWork(obj)
    }
    override suspend fun deleteAndPush(obj: Work) {
        zerdaSource.api.deleteWork(obj.id)
    }
}