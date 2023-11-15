package com.twendev.vulpes.lagopus.model

import android.util.Log

data class Work(
    val number: Int,
    val type: WorkType
) {
    override fun toString(): String {
        return "${type.name} №$number";
    }
    fun isSimular(compareText: String) : Boolean {
        Log.d("Work","IsSimular($compareText) with ${type.name}");
        return (type.shortName.plus(number)).startsWith(compareText.lowercase());
    }
}

sealed class WorkType(val name: String) {
    val shortName = GetShortName();

    private fun GetShortName() : String {
        val workTypeWords = name.lowercase().split(' ')
        var result = "";

        for (word in workTypeWords) {
            result = result.plus(word.firstOrNull())
        }
        Log.d("Work","WorkType.GetShortName($name) = $result");
        return result;
    }

    companion object Static {
        object Lab : WorkType("Лабораторная работа")
        object Pract : WorkType("Практическая работа")
        object Self : WorkType("Самостоятельная работа")
        object PractTask : WorkType("Практическое задание")
    }

    override fun toString(): String {
        return name;
    }
}