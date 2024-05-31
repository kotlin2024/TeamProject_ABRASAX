package com.teamsparta.abrasax.domain.post.comment.controller

import com.teamsparta.abrasax.domain.post.comment.dto.AddCommentRequestDto
import com.teamsparta.abrasax.domain.post.comment.dto.CommentResponseDto
import com.teamsparta.abrasax.domain.post.comment.dto.UpdateCommentRequestDto
import com.teamsparta.abrasax.domain.post.comment.service.CommentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/posts/{postId}/comments")
class CommentController(
    private val commentService: CommentService
) {

    @PostMapping
    fun addComment(
        @PathVariable postId: Long,
        @RequestBody requestDto: AddCommentRequestDto
    ): ResponseEntity<CommentResponseDto> {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(commentService.addComment(postId, requestDto))
    }

    @PutMapping("/{commentId}")
    fun updateComment(
        @PathVariable postId: Long,
        @PathVariable commentId: Long,
        @RequestBody updateCommentRequestDto: UpdateCommentRequestDto
    ): ResponseEntity<CommentResponseDto> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(commentService.updateComment(postId, commentId, updateCommentRequestDto))
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(
        @PathVariable postId: Long,
        @PathVariable commentId: Long
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(commentService.deleteComment(postId, commentId))
    }

    @GetMapping
    fun getCommentList(@PathVariable postId: Long): ResponseEntity<List<CommentResponseDto>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(commentService.getCommentList(postId))
    }
}