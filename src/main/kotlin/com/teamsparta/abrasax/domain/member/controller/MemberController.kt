package com.teamsparta.abrasax.domain.member.controller

import com.teamsparta.abrasax.domain.member.dto.MemberResponse
import com.teamsparta.abrasax.domain.member.dto.SignUpRequest
import com.teamsparta.abrasax.domain.member.dto.UpdatePasswordRequest
import com.teamsparta.abrasax.domain.member.dto.UpdateProfileRequest
import com.teamsparta.abrasax.domain.member.service.MemberService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/members")
@RestController
class MemberController(
    private val memberService: MemberService
) {

    @PostMapping("/")
    fun signUp(@RequestBody signUpRequest: SignUpRequest): ResponseEntity<MemberResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.signUp(signUpRequest))
    }


    @PutMapping("/{memberId}/profile")
    fun updateProfile(
        @PathVariable memberId: Long,
        @RequestBody updateProfileRequest: UpdateProfileRequest
    ): ResponseEntity<MemberResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.updateProfile(memberId, updateProfileRequest))
    }

    @PutMapping("/{memberId}/password")
    fun updatePassword(
        @PathVariable memberId: Long,
        @RequestBody updatePasswordRequest: UpdatePasswordRequest
    ): ResponseEntity<MemberResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.updatePassword(memberId, updatePasswordRequest))
    }

}




