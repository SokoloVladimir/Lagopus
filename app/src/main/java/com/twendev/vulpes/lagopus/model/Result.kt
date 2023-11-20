package com.twendev.vulpes.lagopus.model

import com.google.gson.annotations.SerializedName

data class Result (
    @SerializedName("studentId"     ) var studentId     : Int,
    @SerializedName("workId"        ) var workId        : Int,
    @SerializedName("tasks"         ) var tasks         : ULong,
    @SerializedName("taskCount"     ) var taskCount     : Int
)