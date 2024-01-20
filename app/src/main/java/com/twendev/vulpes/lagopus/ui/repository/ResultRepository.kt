package com.twendev.vulpes.lagopus.ui.repository

import com.twendev.vulpes.lagopus.datasource.ZerdaService
import com.twendev.vulpes.lagopus.model.Result
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class ResultRepository : KoinComponent {
    private val zerdaSource : ZerdaService by inject()

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