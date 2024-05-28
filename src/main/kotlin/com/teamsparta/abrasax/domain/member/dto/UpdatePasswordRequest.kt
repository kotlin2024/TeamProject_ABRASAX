package com.teamsparta.abrasax.domain.member.dto

data class UpdatePasswordRequest(
    val currentPassword: String,
    val newPassword: String,
)