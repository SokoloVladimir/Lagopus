package com.twendev.vulpes.lagopus.model

import com.google.gson.annotations.SerializedName
import com.twendev.vulpes.lagopus.ui.repository.Repositories

data class Result (
    @SerializedName("studentId"     ) val studentId     : Int,
    @SerializedName("workId"        ) val workId        : Int,
    @SerializedName("tasks"         ) val tasks         : ULong,
    @SerializedName("lastEdit"      ) val lastEdit      : String? = null,
    @Transient                        var cachedStudent : Student? = null
) {
    suspend fun cacheStudent() {
        cachedStudent = Repositories.student.get(studentId)
    }

    fun getTaskState(taskNumber: Int) : Boolean {
        return ((1uL).shl(taskNumber).and(tasks)) != 0uL
    }

    fun calculateWithNewTaskState(taskNumber: Int, state: Boolean) : ULong {
        return if (state) calculateWithSet(taskNumber) else calculateWithUnset(taskNumber)
    }

    private fun calculateWithSet(taskNumber: Int) : ULong {
        return 1uL.shl(taskNumber).or(tasks)
    }

    private fun calculateWithUnset(taskNumber: Int) : ULong {
        return 1uL.shl(taskNumber).xor(tasks)
    }
}