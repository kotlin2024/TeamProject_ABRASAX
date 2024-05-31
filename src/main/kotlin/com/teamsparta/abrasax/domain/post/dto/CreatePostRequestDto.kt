package com.teamsparta.abrasax.domain.post.dto



data class CreatePostRequestDto(
    val title: String,
    val content: String,
    val tags: List<String>,
    val authorId: Long,
)
