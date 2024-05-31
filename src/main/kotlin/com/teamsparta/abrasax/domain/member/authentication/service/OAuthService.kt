package com.teamsparta.abrasax.domain.member.authentication.service

import com.teamsparta.abrasax.common.dto.IdResponseDto
import com.teamsparta.abrasax.domain.member.authentication.dto.LoginRequest
import com.teamsparta.abrasax.domain.member.authentication.dto.LoginResponse
import com.teamsparta.abrasax.domain.member.authentication.dto.OAuthSignUpRequest

interface OAuthService {
    fun login(request: LoginRequest): LoginResponse
    fun signUp(request: OAuthSignUpRequest): IdResponseDto
}