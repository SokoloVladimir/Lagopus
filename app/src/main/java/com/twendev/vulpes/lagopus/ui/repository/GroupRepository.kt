package com.twendev.vulpes.lagopus.ui.repository

import com.twendev.vulpes.lagopus.datasource.ZerdaService
import com.twendev.vulpes.lagopus.model.Group

class GroupRepository : RepositoryInterface<Group> {
    private val zerdaSource = ZerdaService.Singleton

    override suspend fun get(id: Int) : Group? {
        return zerdaSource.api.getGroups().firstOrNull { it.id == id}
    }
    override suspend fun get(): List<Group> {
        return zerdaSource.api.getGroups().toList()
    }
    override suspend fun update(obj: Group) {
        return zerdaSource.api.putGroup(obj)
    }
    override suspend fun create(obj: Group) : Group {
        return zerdaSource.api.postGroup(obj)
    }
    override suspend fun delete(obj: Group) {
        zerdaSource.api.deleteGroup(obj.id)
    }
}