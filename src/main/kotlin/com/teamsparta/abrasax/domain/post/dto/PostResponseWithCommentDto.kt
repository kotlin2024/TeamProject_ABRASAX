package com.teamsparta.abrasax.domain.post.dto

import com.teamsparta.abrasax.domain.post.comment.dto.CommentResponseDto

data class PostResponseWithCommentDto(
    var id: Long,
    var title: String,
    var content: String,
    val authorId: Long,
    var tags: List<String>,
    var comments: List<CommentResponseDto>
)

