package com.twendev.vulpes.lagopus.extensions

import com.twendev.vulpes.lagopus.model.Result
import com.twendev.vulpes.lagopus.ui.repository.Repositories

suspend fun Result.cacheStudent() {
    cachedStudent = Repositories.student.get(studentId)
}

suspend fun Result.cacheWork() {
    cachedWork = Repositories.work.get(workId)
}