package com.teamsparta.abrasax.domain.post.dto

import com.teamsparta.abrasax.domain.post.comment.dto.CommentResponseDto

data class PostResponseWithCommentDto(

    var id: Long,
    var title: String,
    var content: String,
    val authorId: Long,
    var tags: String,
    var comments: List<CommentResponseDto>

)

fun toPostWithCommentDtoResponse(
    postResponseDto: PostResponseDto,
    commentResponseDto: List<CommentResponseDto>
): PostResponseWithCommentDto {

    return PostResponseWithCommentDto(

        id = postResponseDto.id!!,
        title = postResponseDto.title,
        content = postResponseDto.content,
        authorId = postResponseDto.authorId,
        tags = postResponseDto.tags.joinToString(",") { it },
        comments = commentResponseDto

    )
}