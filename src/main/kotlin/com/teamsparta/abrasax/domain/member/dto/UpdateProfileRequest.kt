package com.teamsparta.abrasax.domain.member.dto

data class UpdateProfileRequest(
    val socialAccounts: List<String>,
    val nickname: String,
)