package com.teamsparta.abrasax.domain.post.model

import org.springframework.data.domain.Sort

enum class SortDirection {
    ASC, DESC;

    fun toSpringSortDirection(): Sort.Direction {
        return when (this) {
            ASC -> Sort.Direction.ASC
            DESC -> Sort.Direction.DESC
        }
    }

    companion object {
        fun fromString(value: String): SortDirection {
            return try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("Invalid sort direction: $value")
            }
        }
    }
}
