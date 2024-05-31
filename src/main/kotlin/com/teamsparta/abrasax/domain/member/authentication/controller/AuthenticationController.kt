package com.teamsparta.abrasax.domain.member.authentication.controller

import com.teamsparta.abrasax.common.dto.IdResponseDto
import com.teamsparta.abrasax.domain.member.authentication.dto.LoginRequest
import com.teamsparta.abrasax.domain.member.authentication.dto.LoginResponse
import com.teamsparta.abrasax.domain.member.authentication.dto.SignUpRequest
import com.teamsparta.abrasax.domain.member.authentication.dto.UpdatePasswordRequest
import com.teamsparta.abrasax.domain.member.authentication.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthenticationController(
    private val authService: AuthService // 컨트롤러에서 서비스 연결
) {

    @PostMapping("/sign-up")
    fun signUp(@RequestBody signupRequest: SignUpRequest): ResponseEntity<Unit> {
        authService.signUp(signupRequest)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        //authService에서 인증이 성공하면 토큰이 반환되므로 token 변수에 token정보가 담기고 인증이 실패하면 token 변수에 null이 담김
        return ResponseEntity.status(HttpStatus.OK)
            .body(authService.login(loginRequest)) //이 부분에서 인증이 된 상태, 컨트롤러를 따로 만드는게 아닌 기존의 memberController에 추가하기?

    }

    @PutMapping("/{memberId}/password")
    fun updatePassword(
        @AuthenticationPrincipal user: User,
        @PathVariable memberId: Long,
        @RequestBody updatePasswordRequest: UpdatePasswordRequest
    ): ResponseEntity<IdResponseDto> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(authService.updatePassword(user, memberId, updatePasswordRequest))
    }

}