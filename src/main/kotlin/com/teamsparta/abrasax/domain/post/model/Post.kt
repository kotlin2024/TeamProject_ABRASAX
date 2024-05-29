package com.teamsparta.abrasax.domain.post.model

import com.teamsparta.abrasax.domain.helper.ListStringifyHelper
import com.teamsparta.abrasax.domain.member.model.Member
import com.teamsparta.abrasax.domain.post.comment.dto.CommentResponseDto
import com.teamsparta.abrasax.domain.post.dto.PostResponseDto
import com.teamsparta.abrasax.domain.post.dto.PostResponseWithCommentDto
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "post")
class Post(
    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "content", nullable = false)
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member,

    @Column(name = "tags", nullable = false)
    var stringifiedTags: String,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime?,
    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    private fun validateTitle(title: String) {
        require(title.length <= 20) { "제목의 길이는 20자 이하여야 합니다" }
    }

    private fun validateContent(content: String) {
        require(content.length <= 1000) { "내용의 길이는 1000자 이하여야 합니다" }
    }

    private fun validateTags(tags: List<String>) {
        require(tags.all { it.length <= 15 }) { "태그 하나의 길이는 15자 이하여야 합니다" }
    }

    fun update(newTitle: String, newContent: String, newTags: List<String>) {
        validateTitle(newTitle)
        validateContent(newContent)
        validateTags(newTags)

        this.title = newTitle
        this.content = newContent
        this.stringifiedTags = ListStringifyHelper.stringifyList(newTags)
        this.updatedAt = LocalDateTime.now()
    }

    fun delete() {
        deletedAt = LocalDateTime.now()
    }
}

fun Post.toPostResponseDto(): PostResponseDto {
    return PostResponseDto(
        id = id!!,
        title = title,
        content = content,
        tags = ListStringifyHelper.parseToList(stringifiedTags),
        authorId = member.id!!

    )
}

fun Post.toPostWithCommentDtoResponse(
    commentResponseDto: List<CommentResponseDto>
): PostResponseWithCommentDto {
    return PostResponseWithCommentDto(
        id = id!!,
        title = title,
        content = content,
        authorId = member.id!!,
        tags = ListStringifyHelper.parseToList(stringifiedTags),
        comments = commentResponseDto
    )
}