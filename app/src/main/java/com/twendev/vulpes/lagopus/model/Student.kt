package com.twendev.vulpes.lagopus.model

import com.google.gson.annotations.SerializedName

data class Student (
    @SerializedName("id"        ) var id        : Int,
    @SerializedName("surname"   ) var surname   : String,
    @SerializedName("name"      ) var name      : String,
    @SerializedName("patronym"  ) var patronym  : String?  = null,
    @SerializedName("accountId" ) var accountId : Int?     = null,
    @SerializedName("groupId"   ) var groupId   : Int,
    @SerializedName("isDeleted" ) private var isDeleted : Int,
    @SerializedName("account"   ) var account   : Account? = null,
    @SerializedName("group"     ) var group     : Group
) {
    fun getIsDeleted() : Boolean {
        return isDeleted != 0
    }

    fun setIsDeleted(value: Boolean) {
        isDeleted = if (value) 1 else 0
    }

    override fun toString(): String {
        return "${name.first()}. $surname"
    }
}