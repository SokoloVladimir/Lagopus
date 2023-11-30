package com.twendev.vulpes.lagopus.model

import com.google.gson.annotations.SerializedName

data class Student (
    @SerializedName("id"        ) val id        : Int,
    @SerializedName("surname"   ) val surname   : String,
    @SerializedName("name"      ) val name      : String,
    @SerializedName("patronym"  ) val patronym  : String?  = null,
    @SerializedName("accountId" ) val accountId : Int?     = null,
    @SerializedName("groupId"   ) val groupId   : Int,
    @SerializedName("isDeleted" ) val isDeleted : Boolean,
    @SerializedName("account"   ) val account   : Account? = null,
    @SerializedName("group"     ) val group     : Group
) {
    override fun toString(): String {
        return "${name.first()}. $surname"
    }
}