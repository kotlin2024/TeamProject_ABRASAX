package com.teamsparta.abrasax.domain.exception

data class UnauthorizedAccessException(
    val email: String, val modelName: String, val id: Long
) : RuntimeException("Member $email is not authorized to update or delete $modelName (id: $id)")
