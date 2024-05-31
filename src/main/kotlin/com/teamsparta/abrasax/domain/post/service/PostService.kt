package com.teamsparta.abrasax.domain.post.service

import com.teamsparta.abrasax.common.dto.IdResponseDto
import com.teamsparta.abrasax.domain.exception.DeleteNotAllowedException
import com.teamsparta.abrasax.domain.exception.MemberNotFoundException
import com.teamsparta.abrasax.domain.exception.ModelNotFoundException
import com.teamsparta.abrasax.domain.exception.UnauthorizedAccessException
import com.teamsparta.abrasax.domain.member.repository.MemberRepository
import com.teamsparta.abrasax.domain.post.comment.model.toCommentResponseDto
import com.teamsparta.abrasax.domain.post.comment.repository.CommentRepository
import com.teamsparta.abrasax.domain.post.dto.CreatePostRequestDto
import com.teamsparta.abrasax.domain.post.dto.PostResponseDto
import com.teamsparta.abrasax.domain.post.dto.PostResponseWithCommentDto
import com.teamsparta.abrasax.domain.post.dto.UpdatePostRequestDto
import com.teamsparta.abrasax.domain.post.model.*
import com.teamsparta.abrasax.domain.post.repository.PostRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


@Service
class PostService(
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val memberRepository: MemberRepository,
) {

    fun getPosts(
        cursorCreatedAt: LocalDateTime?,
        pageNumber: Int,
        pageSize: Int,
        sortDirection: SortDirection
    ): List<PostResponseDto> {
        val (pageable, cursor) = createPageableAndCursor(cursorCreatedAt, pageNumber, pageSize, sortDirection)


        val posts = if (sortDirection == SortDirection.DESC) {
            postRepository.findByCreatedAtBeforeAndDeletedAtIsNull(cursor, pageable)
        } else {
            postRepository.findByCreatedAtAfterAndDeletedAtIsNull(cursor, pageable)
        }
        return posts.map { it.toPostResponseDto() }
    }

    fun getPostsByTag(
        tag: String,
        cursorCreatedAt: LocalDateTime?,
        pageNumber: Int,
        pageSize: Int,
        sortDirection: SortDirection
    ): List<PostResponseDto> {
        val (pageable, cursor) = createPageableAndCursor(cursorCreatedAt, pageNumber, pageSize, sortDirection)


        val posts = if (sortDirection == SortDirection.DESC) {
            postRepository.findByStringifiedTagsContainingIgnoreCaseAndCreatedAtBeforeAndDeletedAtIsNull(
                tag,
                cursor,
                pageable
            )
        } else {
            postRepository.findByStringifiedTagsContainingIgnoreCaseAndCreatedAtAfterAndDeletedAtIsNull(
                tag,
                cursor,
                pageable
            )
        }
        return posts.map { it.toPostResponseDto() }
    }

    fun getPostById(id: Long): PostResponseWithCommentDto {

        val post =
            postRepository.findPostByIdAndDeletedAtIsNull(id).orElseThrow() { ModelNotFoundException("Post", id) }
        val comments = commentRepository.findAllByPostIdAndDeletedAtIsNull(id).map { it.toCommentResponseDto() }

        return post.toPostWithCommentDtoResponse(comments)
    }

    @Transactional
    fun createPost(user: User, request: CreatePostRequestDto): IdResponseDto {
        val (title, content, tags) = request
        val userEmail = user.username
        val member = memberRepository.findByEmail(userEmail)
            ?: throw MemberNotFoundException(userEmail)

        val post = Post.of(
            title = title,
            content = content,
            tags = tags,
            member = member,
        )

        return postRepository.save(post).toIdResponse()
    }

    @Transactional
    fun updatePost(user: User, id: Long, request: UpdatePostRequestDto): IdResponseDto {
        val (title, content, tags) = request
        val post = postRepository.findByIdOrNull(id) ?: throw ModelNotFoundException("Post", id)
        val userEmail = user.username

        if (userEmail != post.member.email) throw UnauthorizedAccessException(
            email = userEmail,
            modelName = "Post",
            id = id
        )
        post.update(title, content, tags)
        return post.toIdResponse()
    }

    @Transactional
    fun deletePost(user: User, id: Long) {
        val userEmail = user.username
        val post =
            postRepository.findPostByIdAndDeletedAtIsNull(id).orElseThrow { DeleteNotAllowedException("post", id) }

        if (userEmail != post.member.email) throw UnauthorizedAccessException(
            email = userEmail,
            modelName = "Post",
            id = id
        )
        post.delete()
        postRepository.save(post)
    }

    private fun createPageableAndCursor(
        cursorCreatedAt: LocalDateTime?,
        pageNumber: Int,
        pageSize: Int,
        sortDirection: SortDirection
    ): Pair<PageRequest, LocalDateTime> {
        val sort = Sort.by(sortDirection.toSpringSortDirection(), "createdAt")
        val pageable = PageRequest.of(pageNumber, pageSize, sort)
        val cursor = cursorCreatedAt ?: if (sortDirection == SortDirection.ASC) {
            postRepository.findOldestCreatedAt()?.minusSeconds(1) ?: LocalDateTime.MIN
        } else {
            LocalDateTime.now()
        }
        return pageable to cursor
    }
}
