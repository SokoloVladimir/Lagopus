package com.twendev.vulpes.lagopus.model

import com.google.gson.annotations.SerializedName

data class WorkVariant (
    @SerializedName("id"        ) var id        : Int,
    @SerializedName("workId"    ) var workId    : Int,
    @SerializedName("taskCount" ) var taskCount : Int,
    @SerializedName("tasksFor3" ) var tasksFor3 : Int,
    @SerializedName("tasksFor4" ) var tasksFor4 : Int,
    @SerializedName("tasksFor5" ) var tasksFor5 : Int,
    @SerializedName("work"      ) var work      : Work
)