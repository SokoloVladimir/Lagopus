package com.twendev.vulpes.lagopus.model

import com.google.gson.annotations.SerializedName

data class Assignment (
    @SerializedName("workId"       ) var workId       : Int     = 0,
    @SerializedName("groupId"      ) var groupId      : Int     = 0,
    @SerializedName("assignedDate" ) var assignedDate : String? = null,
    @SerializedName("group"        ) var group        : Group?  = Group(),
    @SerializedName("work"         ) var work         : Work?   = Work()
)