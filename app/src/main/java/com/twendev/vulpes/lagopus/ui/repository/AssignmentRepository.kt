package com.twendev.vulpes.lagopus.ui.repository

import com.twendev.vulpes.lagopus.datasource.ZerdaService
import com.twendev.vulpes.lagopus.model.Assignment
import com.twendev.vulpes.lagopus.model.WorkType

class AssignmentRepository : RepositoryInterface<Assignment> {
    private val zerdaSource = ZerdaService.Singleton

    suspend fun get(workId: Int, groupId: Int) : Assignment? {
        return zerdaSource.api.getAssignments().firstOrNull { it.workId == workId && it.groupId == groupId}
    }
    override suspend fun get(): List<Assignment> {
        return zerdaSource.api.getAssignments().toList()
    }
    override suspend fun update(obj: Assignment) {
        return zerdaSource.api.putAssignment(obj)
    }
    override suspend fun create(obj: Assignment) : Assignment {
        return zerdaSource.api.postAssignment(obj)
    }
    override suspend fun delete(obj: Assignment) {
        zerdaSource.api.deleteAssignment(obj.workId, obj.groupId)
    }
}