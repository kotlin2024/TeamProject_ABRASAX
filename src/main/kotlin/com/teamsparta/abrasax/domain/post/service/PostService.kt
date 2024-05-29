package com.teamsparta.abrasax.domain.post.service

import com.teamsparta.abrasax.domain.helper.ListStringifyHelper
import com.teamsparta.abrasax.domain.member.repository.MemberRepository
import com.teamsparta.abrasax.domain.post.comment.model.toCommentResponseDto
import com.teamsparta.abrasax.domain.post.comment.repository.CommentRepository
import com.teamsparta.abrasax.domain.post.dto.CreatePostRequestDto
import com.teamsparta.abrasax.domain.post.dto.PostResponseDto
import com.teamsparta.abrasax.domain.post.dto.PostResponseWithCommentDto
import com.teamsparta.abrasax.domain.post.dto.UpdatePostRequestDto
import com.teamsparta.abrasax.domain.post.model.Post
import com.teamsparta.abrasax.domain.post.model.toPostResponseDto
import com.teamsparta.abrasax.domain.post.model.toPostWithCommentDtoResponse
import com.teamsparta.abrasax.domain.post.repository.PostRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val memberRepository: MemberRepository,
) {
    fun getPosts(): List<PostResponseDto> {
        return postRepository.findAll().map { it.toPostResponseDto() }
    }

    fun getPostById(id: Long): PostResponseWithCommentDto {

        val post = postRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("Post not found")
        val comments = commentRepository.findAllByPostId(id).map { it.toCommentResponseDto() }

        return post.toPostWithCommentDtoResponse(comments)
    }

    @Transactional
    fun createPost(request: CreatePostRequestDto): PostResponseDto {
        val (title, content, tags, authorId) = request
        val author = memberRepository.findByIdOrNull(authorId)
            ?: throw IllegalArgumentException("Member id: ($authorId) is not found")
        val post =
            Post(
                title = title,
                content = content,
                stringifiedTags = ListStringifyHelper.stringifyList(tags),
                author = author
            )

        return postRepository.save(post).toPostResponseDto()
    }

    @Transactional
    fun updatePost(id: Long, request: UpdatePostRequestDto): PostResponseDto {
        val (title, content, tags) = request
        val post = postRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("Post not found")

        post.update(title, content, tags)
        return post.toPostResponseDto()
    }

    @Transactional
    fun deletePost(id: Long) {
        val post = postRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("Post not found")
        commentRepository.deleteAll(commentRepository.findAllByPostId(id))
        postRepository.delete(post)
    }
}