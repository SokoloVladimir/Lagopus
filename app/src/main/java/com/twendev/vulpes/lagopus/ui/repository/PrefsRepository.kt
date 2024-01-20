package com.twendev.vulpes.lagopus.ui.repository

import android.content.SharedPreferences
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PrefsRepository : KoinComponent {
    private val preferences: SharedPreferences by inject()

    fun getString(key: String, defValue: String? = null) : String? {
        return preferences.getString(key, defValue)
    }

    fun updateString(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun getStringSet(key: String, defValue: Set<String>? = null) : Set<String>? {
        return preferences.getStringSet(key, defValue)
    }

    fun updateStringSet(key: String, value: Set<String>) {
        preferences.edit().putStringSet(key, value).apply()
    }

    fun removeFromStringSet(key: String, value: String) : Boolean {
        val set = preferences.getStringSet(key, setOf())!!.toMutableSet();

        var result = false
        if (set.isNotEmpty()) {
            result = set.remove(value)
            updateStringSet(key, set.toSet())
        }
        return result
    }

    fun addToStringSet(key: String, value: String) : Boolean {
        val set = preferences.getStringSet(key, setOf())!!.toMutableSet();
        val result = set.add(value)

        if (result) {
            updateStringSet(key, set.toSet())
        }

        return result;
    }
}