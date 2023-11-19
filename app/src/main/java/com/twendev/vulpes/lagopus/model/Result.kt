package com.twendev.vulpes.lagopus.model

import com.google.gson.annotations.SerializedName

data class Result (
    @SerializedName("studentId"     ) var studentId     : Int,
    @SerializedName("workVariantId" ) var workVariantId : Int,
    @SerializedName("tasks"         ) var tasks         : ArrayList<Int> = arrayListOf(),
    @SerializedName("taskCount"     ) var taskCount     : Int
)