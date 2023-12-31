package com.twendev.vulpes.lagopus.model

import com.google.gson.annotations.SerializedName

data class WorkType (
    @SerializedName("id"   ) val id   : Int = 0,
    @SerializedName("name" ) val name : String = "Новый тип"
) {
    fun getShortName() : String {
        var result = ""
        for (word in name.lowercase().split(' ')) {
            result = result.plus(word.firstOrNull())
        }
        return result
    }

    override fun toString(): String {
        return name
    }
}