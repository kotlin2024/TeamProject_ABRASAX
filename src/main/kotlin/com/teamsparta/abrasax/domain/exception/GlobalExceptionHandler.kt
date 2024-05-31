package com.teamsparta.abrasax.domain.exception.dto

import com.teamsparta.abrasax.domain.exception.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ModelNotFoundException::class)
    fun handleModelNotFoundException(ex: ModelNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(message = ex.message))
    }

    @ExceptionHandler(PasswordNotMatchException::class)
    fun handlePasswordNotMatchException(ex: PasswordNotMatchException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse(message = ex.message))
    }

    @ExceptionHandler(DeleteNotAllowedException::class)
    fun handleDeleteNotAllowedException(ex: DeleteNotAllowedException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse(message = ex.message))
    }

    @ExceptionHandler(DomainInvariantException::class)
    fun handleModelInvariantException(ex: DomainInvariantException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(ex: AuthenticationException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse(message = ex.message))
    }

    @ExceptionHandler(SocialLoginPasswordChangeException::class)
    fun handleSocialLoginPasswordChangeException(ex: SocialLoginPasswordChangeException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse(message = ex.message))
    }

    @ExceptionHandler(EmailDuplicateException::class)
    fun handleEmailDuplicateException(ex: EmailDuplicateException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse(message = ex.message))
    }

    @ExceptionHandler(CommentMismatchException::class)
    fun handleCommentMismatchException(ex: CommentMismatchException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse(message = ex.message))
    }

    @ExceptionHandler(UnauthorizedAccessException::class)
    fun handleUnauthorizedAccessException(ex: UnauthorizedAccessException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse(message = ex.message))
    }
}