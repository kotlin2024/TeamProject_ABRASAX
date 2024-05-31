package com.teamsparta.abrasax.domain.post.comment.controller

import com.teamsparta.abrasax.common.dto.IdResponseDto
import com.teamsparta.abrasax.domain.post.comment.dto.AddCommentRequestDto
import com.teamsparta.abrasax.domain.post.comment.dto.CommentResponseDto
import com.teamsparta.abrasax.domain.post.comment.dto.UpdateCommentRequestDto
import com.teamsparta.abrasax.domain.post.comment.service.CommentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/posts/{postId}/comments")
class CommentController(
    private val commentService: CommentService
) {

    @PostMapping
    fun addComment(
        @AuthenticationPrincipal user: User,
        @PathVariable postId: Long,
        @RequestBody requestDto: AddCommentRequestDto
    ): ResponseEntity<IdResponseDto> {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(commentService.addComment(user, postId, requestDto))
    }

    @PutMapping("/{commentId}")
    fun updateComment(
        @AuthenticationPrincipal user: User,
        @PathVariable postId: Long,
        @PathVariable commentId: Long,
        @RequestBody updateCommentRequestDto: UpdateCommentRequestDto
    ): ResponseEntity<IdResponseDto> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(commentService.updateComment(user, postId, commentId, updateCommentRequestDto))
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(
        @AuthenticationPrincipal user: User,
        @PathVariable postId: Long,
        @PathVariable commentId: Long
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(commentService.deleteComment(user, postId, commentId))
    }

    @GetMapping
    fun getCommentList(@PathVariable postId: Long): ResponseEntity<List<CommentResponseDto>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(commentService.getCommentList(postId))
    }
}