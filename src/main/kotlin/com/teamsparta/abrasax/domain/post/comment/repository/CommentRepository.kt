package com.teamsparta.abrasax.domain.post.comment.repository

import com.teamsparta.abrasax.domain.post.comment.model.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : JpaRepository<Comment, Long> {}