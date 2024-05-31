package com.teamsparta.abrasax.domain.helper

class ListStringifyHelper {
    companion object {
        fun stringifyList(targetList: List<String>): String {
            return targetList.joinToString(",") { it }
        }

        fun parseToList(stringified: String): List<String> {
            return if (stringified == "") listOf() else stringified.split(",")
        }
    }
}
