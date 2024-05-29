package com.teamsparta.abrasax.domain.post.repository

import com.teamsparta.abrasax.domain.post.model.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PostRepository : JpaRepository<Post, Long> {
    fun findPostByIdAndDeletedAtIsNull(id: Long): Optional<Post>
}