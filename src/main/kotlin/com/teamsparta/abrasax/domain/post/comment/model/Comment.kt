package com.teamsparta.abrasax.domain.post.comment.model

import com.teamsparta.abrasax.domain.member.model.Member
import com.teamsparta.abrasax.domain.post.comment.dto.CommentResponseDto
import com.teamsparta.abrasax.domain.post.model.Post
import jakarta.persistence.*
import java.io.InvalidObjectException
import java.time.LocalDateTime

@Entity
@Table(name = "comment")
class Comment(
    @Column(name = "content", nullable = false)
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    val post: Post,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime,

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime?,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun update(newContent: String) {
        validateContentLength(newContent)
        content = newContent
        updatedAt = LocalDateTime.now()
    }

    fun delete() {
        deletedAt = LocalDateTime.now()
    }

    companion object {
        private fun validateContentLength(content: String) {
            if (content.isEmpty() || content.length > 200) {
                throw InvalidObjectException("댓글의 내용은 1자 이상 200자 이하여야합니다.")
            }
        }

        fun of(content: String, member: Member, post: Post): Comment {
            validateContentLength(content)
            val timestamp = LocalDateTime.now()
            return Comment(
                content = content,
                member = member,
                post = post,
                createdAt = timestamp,
                updatedAt = timestamp,
                deletedAt = null
            )
        }
    }
}

fun Comment.toCommentResponseDto(): CommentResponseDto {
    return CommentResponseDto(
        id = id!!,
        content = content,
        authorId = member.id!!,
    )
}