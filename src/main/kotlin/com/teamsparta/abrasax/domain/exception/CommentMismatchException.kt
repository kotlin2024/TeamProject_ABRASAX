package com.teamsparta.abrasax.domain.exception

data class CommentMismatchException(val commentId: Long, val postId: Long) :
    RuntimeException("해당 게시글에 달린 댓글이 아닙니다. postId=$postId commentId=$commentId")