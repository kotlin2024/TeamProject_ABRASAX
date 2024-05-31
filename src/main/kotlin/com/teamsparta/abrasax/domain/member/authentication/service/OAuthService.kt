package com.teamsparta.abrasax.domain.member.authentication.service

import com.teamsparta.abrasax.domain.member.authentication.dto.LoginRequest
import com.teamsparta.abrasax.domain.member.authentication.dto.LoginResponse
import com.teamsparta.abrasax.domain.member.authentication.dto.OAuthSignUpRequest
import com.teamsparta.abrasax.domain.member.dto.MemberResponse

interface OAuthService {
    fun login(request: LoginRequest): LoginResponse
    fun signUp(request: OAuthSignUpRequest): MemberResponse
}