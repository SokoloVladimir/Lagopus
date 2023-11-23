package com.twendev.vulpes.lagopus.model

import com.google.gson.annotations.SerializedName

data class Semester (
    @SerializedName("id"        ) val id        : Int,
    @SerializedName("startYear" ) val startYear : Int,
    @SerializedName("isSecond"  ) val isSecond  : Boolean
)