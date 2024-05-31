package com.teamsparta.abrasax.domain.member.service

import com.teamsparta.abrasax.common.dto.IdResponseDto
import com.teamsparta.abrasax.domain.exception.MemberNotFoundException
import com.teamsparta.abrasax.domain.exception.UnauthorizedAccessException
import com.teamsparta.abrasax.domain.member.dto.UpdateProfileRequest
import com.teamsparta.abrasax.domain.member.model.toIdResponseDto
import com.teamsparta.abrasax.domain.member.repository.MemberRepository
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(private val memberRepository: MemberRepository) {
    @Transactional
    fun updateProfile(user: User, id: Long, request: UpdateProfileRequest): IdResponseDto {
        val userEmail = user.username
        val member =
            memberRepository.findByEmail(userEmail) ?: throw MemberNotFoundException(userEmail)
        if (member.id != id) {
            throw UnauthorizedAccessException(email = userEmail, modelName = "Member", id = id)
        }
        val (socialAccounts, nickname) = request
        member.updateProfile(newSocialAccounts = socialAccounts, newNickname = nickname)
        return member.toIdResponseDto()
    }
}