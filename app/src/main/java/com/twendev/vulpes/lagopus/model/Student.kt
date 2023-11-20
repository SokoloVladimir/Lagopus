package com.twendev.vulpes.lagopus.model

import com.google.gson.annotations.SerializedName

data class Student (
    @SerializedName("id"        ) var id        : Int,
    @SerializedName("surname"   ) var surname   : String,
    @SerializedName("name"      ) var name      : String,
    @SerializedName("patronym"  ) var patronym  : String?  = null,
    @SerializedName("accountId" ) var accountId : Int?     = null,
    @SerializedName("groupId"   ) var groupId   : Int,
    @SerializedName("isDeleted" ) var isDeleted : Boolean,
    @SerializedName("account"   ) var account   : Account? = null,
    @SerializedName("group"     ) var group     : Group
) {
    override fun toString(): String {
        return "${name.first()}. $surname"
    }
}