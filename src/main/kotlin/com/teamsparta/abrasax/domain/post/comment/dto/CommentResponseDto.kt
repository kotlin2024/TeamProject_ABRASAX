package com.teamsparta.abrasax.domain.post.comment.dto

data class CommentResponseDto(
    val id: Long,
    val content: String,
    val authorId: Long
)