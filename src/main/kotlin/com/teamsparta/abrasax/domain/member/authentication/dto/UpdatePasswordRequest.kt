package com.teamsparta.abrasax.domain.member.authentication.dto

data class UpdatePasswordRequest(
    val currentPassword: String,
    val newPassword: String,
)