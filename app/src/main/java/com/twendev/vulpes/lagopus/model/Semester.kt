package com.twendev.vulpes.lagopus.model

import com.google.gson.annotations.SerializedName

data class Semester (
    @SerializedName("id"        ) var id        : Int,
    @SerializedName("startYear" ) var startYear : Int,
    @SerializedName("isSecond"  ) var isSecond  : Boolean
)