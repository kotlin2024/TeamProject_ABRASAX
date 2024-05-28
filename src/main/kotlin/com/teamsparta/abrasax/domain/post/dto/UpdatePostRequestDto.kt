package com.teamsparta.abrasax.domain.post.dto

data class UpdatePostRequestDto(
    val title: String,
    val content: String,
    val tags: List<String>
)
