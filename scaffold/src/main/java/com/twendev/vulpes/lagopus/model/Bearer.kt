package com.twendev.vulpes.lagopus.model

import com.google.gson.annotations.SerializedName

data class Bearer (
    @SerializedName("access_token" ) var accessToken : String,
    @SerializedName("username"     ) var username    : String,
    @SerializedName("role"         ) var role        : String
)