package com.teamsparta.abrasax.domain.exception

data class EmailDuplicateException(val email: String) : RuntimeException("Email: $email already exists.")
