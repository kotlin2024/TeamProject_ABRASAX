package com.teamsparta.abrasax.domain.member.dto

data class SignUpRequest(
    val email: String,
    val password: String,
    val nickname: String,
)