package com.teamsparta.abrasax.domain.member.authentication.dto

data class OAuthSignUpRequest(
    val email: String,
    val password: String,
    val nickname: String,
    val socialProvider: String,
)
