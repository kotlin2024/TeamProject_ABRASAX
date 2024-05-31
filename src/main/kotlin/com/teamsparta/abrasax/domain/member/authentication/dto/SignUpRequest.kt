package com.teamsparta.abrasax.domain.member.authentication.dto


data class SignUpRequest(
    val email: String,
    val password: String,
    val nickname: String,
)