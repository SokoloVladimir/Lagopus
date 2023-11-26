package com.twendev.vulpes.lagopus.model

import com.google.gson.annotations.SerializedName

data class Semester (
    @SerializedName("id"        ) val id        : Int = 0,
    @SerializedName("startYear" ) val startYear : Int = 2010,
    @SerializedName("isSecond"  ) val isSecond  : Boolean = false
) {
    override fun toString(): String {
        return "${startYear.toString().takeLast(2)}-${(startYear + 1).toString().takeLast(2)}/${if (isSecond) "2" else "1"}"
    }
}