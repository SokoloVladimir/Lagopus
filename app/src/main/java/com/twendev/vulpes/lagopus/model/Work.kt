package com.twendev.vulpes.lagopus.model

import android.util.Log
import com.google.gson.annotations.SerializedName

data class Work (
    @SerializedName("id"           ) var id           : Int,
    @SerializedName("disciplineId" ) var disciplineId : Int,
    @SerializedName("workTypeId"   ) var workTypeId   : Int,
    @SerializedName("semesterId"   ) var semesterId   : Int,
    @SerializedName("number"       ) var number       : Int,
    @SerializedName("theme"        ) var theme        : String?     = null,
    @SerializedName("taskCount"    ) var taskCount    : Int,
    @SerializedName("taskFor3"     ) var taskFor3     : Int,
    @SerializedName("taskFor4"     ) var taskFor4     : Int,
    @SerializedName("taskFor5"     ) var taskFor5     : Int,
    @SerializedName("discipline"   ) var discipline   : Discipline,
    @SerializedName("semester"     ) var semester     : Semester,
    @SerializedName("workType"     ) var workType     : WorkType
) {
    fun isSimular(compareText: String) : Boolean {
        Log.d("Work","IsSimular($compareText) with ${workType.name}")
        return (workType.getShortName().plus(number)).startsWith(compareText.lowercase())
    }
    override fun toString(): String {
        return "${workType.getShortName()}$number $theme"
    }
}
