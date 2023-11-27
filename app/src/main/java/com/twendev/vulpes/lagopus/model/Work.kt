package com.twendev.vulpes.lagopus.model

import android.util.Log
import com.google.gson.annotations.SerializedName

data class Work (
    @SerializedName("id"           ) val id           : Int         = 0,
    @SerializedName("disciplineId" ) val disciplineId : Int         = 0,
    @SerializedName("workTypeId"   ) val workTypeId   : Int         = 0,
    @SerializedName("semesterId"   ) val semesterId   : Int         = 0,
    @SerializedName("number"       ) val number       : Int         = 0,
    @SerializedName("theme"        ) val theme        : String      = "",
    @SerializedName("taskCount"    ) val taskCount    : Int         = 5,
    @SerializedName("taskFor3"     ) val taskFor3     : Int         = 3,
    @SerializedName("taskFor4"     ) val taskFor4     : Int         = 4,
    @SerializedName("taskFor5"     ) val taskFor5     : Int         = 5,
    @SerializedName("discipline"   ) val discipline   : Discipline? = null,
    @SerializedName("semester"     ) val semester     : Semester?   = null,
    @SerializedName("workType"     ) val workType     : WorkType?   = null
) {
    fun isSimular(compareText: String) : Boolean {
        Log.d("Work","IsSimular($compareText) with ${workType?.name}")
        return (workType?.getShortName().plus(number)).startsWith(compareText.lowercase())
    }
    override fun toString(): String {
        return "${workType?.getShortName()}$number $theme"
    }
}
