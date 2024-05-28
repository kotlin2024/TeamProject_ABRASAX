package com.teamsparta.abrasax.domain.member.dto

data class MemberResponse(
    val id: Long,
    val email: String,
    val nickname: String,
    val socialAccounts: List<String>,
)
