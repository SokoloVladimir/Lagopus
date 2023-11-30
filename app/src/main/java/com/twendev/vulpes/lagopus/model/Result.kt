package com.twendev.vulpes.lagopus.model

import com.google.gson.annotations.SerializedName

data class Result (
    @SerializedName("studentId"     ) val studentId     : Int,
    @SerializedName("workId"        ) val workId        : Int,
    @SerializedName("tasks"         ) val tasks         : ULong,
    @SerializedName("taskCount"     ) val taskCount     : Int
)