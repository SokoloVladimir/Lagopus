package com.twendev.vulpes.lagopus.model

import com.google.gson.annotations.SerializedName

data class Group (
    @SerializedName("id"   ) val id   : Int,
    @SerializedName("name" ) val name : String
)