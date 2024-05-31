package com.teamsparta.abrasax.domain.member.authentication.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class NaverUserDataResponse(
    @JsonProperty("resultcode")
    val resultCode: String,
    val message: String,
    val response: NaverUserData
)

data class NaverUserData(
    val id: String,
    val email: String,
    val name: String,
    val nickname: String,
)
