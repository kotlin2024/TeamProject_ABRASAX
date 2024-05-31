package com.teamsparta.abrasax.domain.exception

data class MemberNotFoundException(val email: String) : RuntimeException("Member with email: $email not found")
