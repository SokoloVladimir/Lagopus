package com.twendev.vulpes.lagopus.model

import android.util.Log
import com.google.gson.annotations.SerializedName

data class Work (
    @SerializedName("id"           ) var id           : Int,
    @SerializedName("disciplineId" ) var disciplineId : Int,
    @SerializedName("workTypeId"   ) var workTypeId   : Int,
    @SerializedName("number"       ) var number       : Int,
    @SerializedName("theme"        ) var theme        : String?     = null,
    @SerializedName("dateEst"      ) var dateEst      : String?     = null,
    @SerializedName("taskCount"    ) var taskCount    : Int,
    @SerializedName("discipline"   ) var discipline   : Discipline,
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
