package com.teamsparta.abrasax.domain.member.controller

import com.teamsparta.abrasax.common.dto.IdResponseDto
import com.teamsparta.abrasax.domain.member.dto.UpdateProfileRequest
import com.teamsparta.abrasax.domain.member.service.MemberService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.*

@RequestMapping("/members")
@RestController
class MemberController(
    private val memberService: MemberService,
) {

    @PutMapping("/{memberId}/profile")
    fun updateProfile(
        @AuthenticationPrincipal user: User,
        @PathVariable memberId: Long,
        @RequestBody updateProfileRequest: UpdateProfileRequest
    ): ResponseEntity<IdResponseDto> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(memberService.updateProfile(user, memberId, updateProfileRequest))
    }

}




