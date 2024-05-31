package com.teamsparta.abrasax.domain.post.model

import com.teamsparta.abrasax.domain.exception.ModelInvariantException
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
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime,
    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null,

    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun update(newTitle: String, newContent: String, newTags: List<String>) {
        validateTitleLength(newTitle)
        validateContentLength(newContent)
        validateTagListSize(newTags)
        validateTagLength(newTags)
        validateNoDuplicateTags(newTags)

        this.title = newTitle
        this.content = newContent
        this.stringifiedTags = ListStringifyHelper.stringifyList(newTags)
        this.updatedAt = LocalDateTime.now()
    }

    fun delete() {
        deletedAt = LocalDateTime.now()
    }

    companion object {
        private fun validateTitleLength(title: String) {
            if (title.isEmpty() || title.length > 20) {
                throw ModelInvariantException.InvalidTitleException("제목은 비어있지 않고 20자 이하여야 합니다.")
            }
        }

        private fun validateContentLength(content: String) {
            if (content.isEmpty() || content.length > 1000) {
                throw ModelInvariantException.InvalidContentException("내용은 비어있지 않고 1000자 이하여야 합니다.")
            }
        }

        private fun validateTagListSize(tags: List<String>) {
            if (tags.size > 5)
                throw ModelInvariantException.InvalidTagSizeException("태그는 5개를 초과할 수 없습니다.")
        }

        private fun validateTagLength(tags: List<String>) {
            if (tags.any { it.length > 15 })
                throw ModelInvariantException.InvalidTagLengthException("태그는 15자 이하여야 합니다.")
        }

        private fun validateNoDuplicateTags(tags: List<String>) {
            val distinctTags = tags.distinct()
            if (distinctTags.size != tags.size) {
                throw ModelInvariantException.InvalidDuplicateTagException("중복된 태그는 생성이 불가능합니다.")
            }
        }


        fun of(title: String, content: String, member: Member, tags: List<String>): Post {
            validateTitleLength(title)
            validateContentLength(content)
            validateTagListSize(tags)
            validateTagLength(tags)
            validateNoDuplicateTags(tags)

            val timestamp = LocalDateTime.now()

            return Post(
                title = title,
                content = content,
                member = member,
                stringifiedTags = ListStringifyHelper.stringifyList(tags),
                createdAt = timestamp,
                updatedAt = timestamp,
                deletedAt = null
            )
        }
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