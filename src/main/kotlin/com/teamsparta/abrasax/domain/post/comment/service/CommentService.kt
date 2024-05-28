package com.teamsparta.abrasax.domain.post.comment.service

import com.teamsparta.abrasax.domain.post.comment.dto.AddCommentRequestDto
import com.teamsparta.abrasax.domain.post.comment.dto.CommentResponseDto
import com.teamsparta.abrasax.domain.post.comment.dto.UpdateCommentRequestDto
import com.teamsparta.abrasax.domain.post.comment.model.Comment
import com.teamsparta.abrasax.domain.post.comment.model.toCommentResponseDto
import com.teamsparta.abrasax.domain.post.comment.repository.CommentRepository
import com.teamsparta.abrasax.domain.post.repository.PostRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository
) {
    @Transactional
    fun addComment(postId: Long, request: AddCommentRequestDto): CommentResponseDto {
        val post = postRepository.findByIdOrNull(postId) ?: throw IllegalArgumentException("Post")

        val comment = Comment(
            content = request.content,
            authorId = request.authorId,
            post = post
        )
        return commentRepository.save(comment).toCommentResponseDto()
    }

    @Transactional
    fun updateComment(postId: Long, commentId: Long, requestDto: UpdateCommentRequestDto): CommentResponseDto {

        val post = postRepository.findByIdOrNull(postId) ?: throw IllegalArgumentException("Post")
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw IllegalArgumentException("Comment")

        comment.update(requestDto.content)
        return comment.toCommentResponseDto()
    }

    @Transactional
    fun deleteComment(commentId: Long) {
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw IllegalArgumentException("Comment")
        commentRepository.delete(comment)
    }

    fun getCommentList(postId: Long): List<CommentResponseDto> {

        return commentRepository.findAllByPostId(postId).map { it.toCommentResponseDto() }
    }

}