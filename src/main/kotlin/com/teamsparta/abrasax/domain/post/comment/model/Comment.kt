package com.teamsparta.abrasax.domain.post.comment.model

import com.teamsparta.abrasax.domain.member.model.Member
import com.teamsparta.abrasax.domain.post.comment.dto.CommentResponseDto
import com.teamsparta.abrasax.domain.post.model.Post
import jakarta.persistence.*

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
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun update(newContent: String) {
        content = newContent
    }
}

fun Comment.toCommentResponseDto(): CommentResponseDto {
    return CommentResponseDto(
        id = id!!,
        content = content,
        authorId = member.id!!,
    )
}