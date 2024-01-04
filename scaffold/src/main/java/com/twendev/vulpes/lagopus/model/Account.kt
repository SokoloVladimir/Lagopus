package com.twendev.vulpes.lagopus.model

import com.google.gson.annotations.SerializedName

data class Account (
    @SerializedName("id"    ) val id    : Int,
    @SerializedName("login" ) val login : String
)