package com.teamsparta.abrasax.domain.post.model

import com.teamsparta.abrasax.domain.helper.ListStringifyHelper
import com.teamsparta.abrasax.domain.member.model.Member
import com.teamsparta.abrasax.domain.post.comment.dto.CommentResponseDto
import com.teamsparta.abrasax.domain.post.dto.PostResponseDto
import com.teamsparta.abrasax.domain.post.dto.PostResponseWithCommentDto
import jakarta.persistence.*

@Entity
@Table(name = "post")
class Post(
    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "content", nullable = false)
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    val author: Member,

    @Column(name = "tags", nullable = false)
    var stringifiedTags: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun update(newTitle: String, newContent: String, newTags: List<String>) {
        title = newTitle
        content = newContent
        stringifiedTags = ListStringifyHelper.stringifyList(newTags)
    }

}

fun Post.toPostResponseDto(): PostResponseDto {
    return PostResponseDto(
        id = id!!,
        title = title,
        content = content,
        tags = ListStringifyHelper.parseToList(stringifiedTags),
        authorId = author.id!!
    )
}

fun Post.toPostWithCommentDtoResponse(
    commentResponseDto: List<CommentResponseDto>
): PostResponseWithCommentDto {

    return PostResponseWithCommentDto(

        id = id!!,
        title = title,
        content = content,
        authorId = author.id!!,
        tags = ListStringifyHelper.parseToList(stringifiedTags),
        comments = commentResponseDto

    )
}