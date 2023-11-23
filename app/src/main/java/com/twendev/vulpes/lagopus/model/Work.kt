package com.twendev.vulpes.lagopus.model

import android.util.Log
import com.google.gson.annotations.SerializedName

data class Work (
    @SerializedName("id"           ) val id           : Int,
    @SerializedName("disciplineId" ) val disciplineId : Int,
    @SerializedName("workTypeId"   ) val workTypeId   : Int,
    @SerializedName("semesterId"   ) val semesterId   : Int,
    @SerializedName("number"       ) val number       : Int,
    @SerializedName("theme"        ) val theme        : String?     = null,
    @SerializedName("taskCount"    ) val taskCount    : Int,
    @SerializedName("taskFor3"     ) val taskFor3     : Int,
    @SerializedName("taskFor4"     ) val taskFor4     : Int,
    @SerializedName("taskFor5"     ) val taskFor5     : Int,
    @SerializedName("discipline"   ) val discipline   : Discipline,
    @SerializedName("semester"     ) val semester     : Semester,
    @SerializedName("workType"     ) val workType     : WorkType
) {
    fun isSimular(compareText: String) : Boolean {
        Log.d("Work","IsSimular($compareText) with ${workType.name}")
        return (workType.getShortName().plus(number)).startsWith(compareText.lowercase())
    }
    override fun toString(): String {
        return "${workType.getShortName()}$number $theme"
    }
}
