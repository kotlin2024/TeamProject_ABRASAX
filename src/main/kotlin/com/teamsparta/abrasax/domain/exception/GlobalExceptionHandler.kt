package com.teamsparta.abrasax.domain.exception.dto

import com.teamsparta.abrasax.domain.exception.DeleteNotAllowedException
import com.teamsparta.abrasax.domain.exception.ModelNotFoundException
import com.teamsparta.abrasax.domain.exception.PasswordNotMatchException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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

//    @ExceptionHandler(MemberNotFoundException::class)
//    fun handleMemberNotFoundException(ex: MemberNotFoundException): ResponseEntity<ErrorResponse> {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(message = ex.message))
//    }

}