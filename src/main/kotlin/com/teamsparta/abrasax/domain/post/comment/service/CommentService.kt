package com.teamsparta.abrasax.domain.post.comment.service



import com.teamsparta.abrasax.common.dto.IdResponseDto
import com.teamsparta.abrasax.domain.exception.CommentMismatchException
import com.teamsparta.abrasax.domain.exception.MemberNotFoundException
import com.teamsparta.abrasax.domain.exception.ModelNotFoundException
import com.teamsparta.abrasax.domain.exception.UnauthorizedAccessException
import com.teamsparta.abrasax.domain.member.repository.MemberRepository
import com.teamsparta.abrasax.domain.post.comment.dto.AddCommentRequestDto
import com.teamsparta.abrasax.domain.post.comment.dto.CommentResponseDto
import com.teamsparta.abrasax.domain.post.comment.dto.UpdateCommentRequestDto
import com.teamsparta.abrasax.domain.post.comment.model.Comment
import com.teamsparta.abrasax.domain.post.comment.model.toCommentResponseDto
import com.teamsparta.abrasax.domain.post.comment.model.toIdResponseDto
import com.teamsparta.abrasax.domain.post.comment.repository.CommentRepository
import com.teamsparta.abrasax.domain.post.repository.PostRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
    private val memberRepository: MemberRepository
) {
    @Transactional
    fun addComment(user: User, postId: Long, request: AddCommentRequestDto): IdResponseDto {
        val post =
            postRepository.findPostByIdAndDeletedAtIsNull(postId).orElseThrow { ModelNotFoundException("Post", postId) }
        val (content) = request
        val userEmail = user.username
        val member = memberRepository.findByEmail(userEmail) ?: throw MemberNotFoundException(userEmail)

        val comment = Comment.of(
            content = content,
            member = member,
            post = post,
        )
        return commentRepository.save(comment).toIdResponseDto()
    }

    @Transactional

    fun updateComment(user: User, postId: Long, commentId: Long, requestDto: UpdateCommentRequestDto): IdResponseDto {
        if (postRepository.existsByIdAndDeletedAtIsNull(postId) == false)
            throw ModelNotFoundException("Post", postId)
        val comment = commentRepository.findByIdAndDeletedAtIsNull(commentId) ?: throw ModelNotFoundException(
            "Comment",
            commentId
        )
        val userEmail = user.username
        if (comment.member.email != userEmail) throw UnauthorizedAccessException(
            email = userEmail,
            modelName = "Comment",
            id = commentId
        )
        comment.update(requestDto.content)
        return comment.toIdResponseDto()
    }

    @Transactional

    fun deleteComment(user: User, postId: Long, commentId: Long) {
        val post = postRepository.findByIdOrNull(postId) ?: throw ModelNotFoundException("Post", postId)
        if (post.id != postId) throw CommentMismatchException(postId, commentId)
        val comment = commentRepository.findByIdAndDeletedAtIsNull(commentId)
            ?: throw ModelNotFoundException("Comment", commentId)

        val userEmail = user.username
        if (comment.member.email != userEmail) throw UnauthorizedAccessException(
            email = userEmail,
            modelName = "Comment",
            id = commentId
        )

        comment.delete()
    }

    fun getCommentList(postId: Long): List<CommentResponseDto> {
        return commentRepository.findAllByPostIdAndDeletedAtIsNull(postId).map { it.toCommentResponseDto() }
    }

}