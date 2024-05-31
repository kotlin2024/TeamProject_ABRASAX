package com.teamsparta.abrasax.domain.post.repository

import com.teamsparta.abrasax.domain.post.model.Post
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface PostRepository : JpaRepository<Post, Long> {
    fun findPostByIdAndDeletedAtIsNull(id: Long): Optional<Post>
    fun findByStringifiedTagsContainingIgnoreCaseAndCreatedAtBeforeAndDeletedAtIsNull(
        tag: String,
        cursorCreatedAt: LocalDateTime,
        pageable: Pageable
    ): List<Post>

    fun findByStringifiedTagsContainingIgnoreCaseAndCreatedAtAfterAndDeletedAtIsNull(
        tag: String,
        cursorCreatedAt: LocalDateTime,
        pageable: Pageable
    ): List<Post>


    fun findByCreatedAtBeforeAndDeletedAtIsNull(cursorCreatedAt: LocalDateTime, pageable: Pageable): List<Post>
    fun findByCreatedAtAfterAndDeletedAtIsNull(cursorCreatedAt: LocalDateTime, pageable: Pageable): List<Post>

    @Query("SELECT MIN(p.createdAt) FROM Post p WHERE p.deletedAt IS NULL")
    fun findOldestCreatedAt(): LocalDateTime?

    fun existsByIdAndDeletedAtIsNull(id: Long): Boolean
}
