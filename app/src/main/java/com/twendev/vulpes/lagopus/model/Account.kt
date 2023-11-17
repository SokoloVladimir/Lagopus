package com.twendev.vulpes.lagopus.model

import com.google.gson.annotations.SerializedName

data class Account (
    @SerializedName("id"    ) var id    : Int,
    @SerializedName("login" ) var login : String
)