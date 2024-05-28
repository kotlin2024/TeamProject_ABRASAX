package com.teamsparta.abrasax.domain.post.comment.service

import com.teamsparta.abrasax.domain.post.comment.dto.AddCommentRequestDto
import com.teamsparta.abrasax.domain.post.comment.dto.CommentResponseDto
import com.teamsparta.abrasax.domain.post.comment.model.Comment
import com.teamsparta.abrasax.domain.post.comment.model.toCommentResponseDto
import com.teamsparta.abrasax.domain.post.comment.repository.CommentRepository
import com.teamsparta.abrasax.domain.post.repository.PostRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository
) {
    fun addComment(postId: Long, request: AddCommentRequestDto): CommentResponseDto {
        val post = postRepository.findByIdOrNull(postId) ?: throw IllegalArgumentException("Post")

        val comment = Comment(
            content = request.content,
            authorId = request.authorId,
            post = post
        )
        return commentRepository.save(comment).toCommentResponseDto()
    }

//    fun updateComment(id: Long, requestDto: updateCommentRequestDto) {
//        TODO()
//    }
}