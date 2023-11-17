package com.twendev.vulpes.lagopus.model

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("id"          ) var id          : Int,
    @SerializedName("surname"     ) var surname     : String,
    @SerializedName("name"        ) var name        : String,
    @SerializedName("midname"     ) var midname     : String,
    @SerializedName("accountId"   ) var accountId   : Int?     = null,
    @SerializedName("groupId"     ) var groupId     : Int,
    @SerializedName("isDarkTheme" ) var isDarkTheme : Int,
    @SerializedName("account"     ) var account     : Account? = null,
    @SerializedName("group"       ) var group       : Group
) {
    override fun toString(): String {
        return "${name.first()}. ${midname.first()}. $surname"
    }
}