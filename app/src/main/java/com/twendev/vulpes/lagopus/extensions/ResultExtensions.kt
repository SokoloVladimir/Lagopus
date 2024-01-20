package com.twendev.vulpes.lagopus.extensions

import com.twendev.vulpes.lagopus.model.Result
import com.twendev.vulpes.lagopus.ui.repository.StudentRepository
import com.twendev.vulpes.lagopus.ui.repository.WorkRepository

suspend fun Result.cacheStudent() {
    cachedStudent = StudentRepository().get(studentId)
}

suspend fun Result.cacheWork() {
    cachedWork = WorkRepository().get(workId)
}