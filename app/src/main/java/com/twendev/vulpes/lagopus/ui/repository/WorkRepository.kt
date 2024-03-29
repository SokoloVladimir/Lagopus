package com.twendev.vulpes.lagopus.ui.repository

import com.twendev.vulpes.lagopus.datasource.ZerdaService
import com.twendev.vulpes.lagopus.model.Work
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WorkRepository : RepositoryInterface<Work>, KoinComponent {
    private val zerdaSource : ZerdaService by inject()

    suspend fun get(id: Int) : Work? {
        return zerdaSource.api.getWorks().firstOrNull { it.id == id}
    }
    override suspend fun get(): List<Work> {
        return zerdaSource.api.getWorks().toList()
    }
    override suspend fun update(obj: Work) {
        return zerdaSource.api.putWork(obj)
    }
    override suspend fun create(obj: Work) : Work {
        return zerdaSource.api.postWork(obj)
    }
    override suspend fun delete(obj: Work) {
        zerdaSource.api.deleteWork(obj.id)
    }
}