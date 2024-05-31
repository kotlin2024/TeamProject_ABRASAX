package com.teamsparta.abrasax.domain.post.controller

import com.teamsparta.abrasax.common.dto.IdResponseDto
import com.teamsparta.abrasax.domain.post.dto.CreatePostRequestDto
import com.teamsparta.abrasax.domain.post.dto.PostResponseDto
import com.teamsparta.abrasax.domain.post.dto.PostResponseWithCommentDto
import com.teamsparta.abrasax.domain.post.dto.UpdatePostRequestDto
import com.teamsparta.abrasax.domain.post.model.SortDirection
import com.teamsparta.abrasax.domain.post.service.PostService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/posts")
class PostController(private val postService: PostService) {

    @GetMapping
    fun getPosts(
        @RequestParam(required = false) cursorCreatedAt: LocalDateTime?,
        @RequestParam(defaultValue = "0") pageNumber: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(defaultValue = "DESC") sortDirection: String,
    ): ResponseEntity<List<PostResponseDto>> {
        val direction = SortDirection.fromString(sortDirection)
        val posts = postService.getPosts(cursorCreatedAt, pageNumber, pageSize, direction)
        return ResponseEntity.status(HttpStatus.OK).body(posts)
    }

    @GetMapping("/{postId}")
    fun getPostById(@PathVariable("postId") id: Long): ResponseEntity<PostResponseWithCommentDto> {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostById(id))
    }

    @GetMapping("/tag/{tagName}")
    fun getPostsByTag(
        @PathVariable tagName: String,
        @RequestParam(required = false) cursorCreatedAt: LocalDateTime?,
        @RequestParam(defaultValue = "0") pageNumber: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(defaultValue = "DESC") sortDirection: String,
    ): ResponseEntity<List<PostResponseDto>> {
        val direction = SortDirection.fromString(sortDirection)
        val posts = postService.getPostsByTag(tagName, cursorCreatedAt, pageNumber, pageSize, direction)
        return ResponseEntity.status(HttpStatus.OK).body(posts)
    }

    @PostMapping
    fun createPost(
        @AuthenticationPrincipal user: User,
        @RequestBody createPostRequestDto: CreatePostRequestDto
    ): ResponseEntity<IdResponseDto> {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(user, createPostRequestDto))
    }

    @PutMapping("/{postId}")
    fun updatePost(
        @AuthenticationPrincipal user: User,
        @PathVariable("postId") id: Long,
        @RequestBody updatePostRequestDto: UpdatePostRequestDto
    ): ResponseEntity<IdResponseDto> {
        return ResponseEntity.status(HttpStatus.OK).body(postService.updatePost(user, id, updatePostRequestDto))
    }

    @DeleteMapping("/{postId}")
    fun deletePost(
        @AuthenticationPrincipal user: User,
        @PathVariable("postId") id: Long
    ): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.OK).body(postService.deletePost(user, id))
    }


}