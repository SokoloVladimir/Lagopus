package com.twendev.vulpes.lagopus.ui.repository

import com.twendev.vulpes.lagopus.datasource.ZerdaService
import com.twendev.vulpes.lagopus.model.Result as Result


class ResultRepository {
    private val zerdaSource = ZerdaService.Singleton

    suspend fun get(studentId: Int? = null, workId: Int? = null, groupId: Int? = null) : List<Result> {
        return zerdaSource.api.getResults(
            studentId = studentId,
            workId = workId,
            groupId = groupId
        ).toList()
    }
    suspend fun update(obj: Result) {
        return zerdaSource.api.postResult(obj.studentId, obj.workId, obj.tasks)
    }
}