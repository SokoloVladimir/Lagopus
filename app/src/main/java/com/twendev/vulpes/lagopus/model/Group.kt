package com.twendev.vulpes.lagopus.model

import com.google.gson.annotations.SerializedName

data class Group (
    @SerializedName("id"   ) var id   : Int,
    @SerializedName("name" ) var name : String
)