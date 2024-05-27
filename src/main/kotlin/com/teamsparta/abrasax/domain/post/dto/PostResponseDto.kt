package com.teamsparta.abrasax.domain.post.dto

data class PostResponseDto(
    val id: Long,
    val title: String,
    val content: String,
    val tags: List<String>,
    val authorId: Long,
)
