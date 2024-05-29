package com.teamsparta.abrasax.domain.member.service

import com.teamsparta.abrasax.domain.exception.MemberNotFoundException
import com.teamsparta.abrasax.domain.exception.PasswordNotMatchException
import com.teamsparta.abrasax.domain.member.dto.MemberResponse
import com.teamsparta.abrasax.domain.member.dto.SignUpRequest
import com.teamsparta.abrasax.domain.member.dto.UpdatePasswordRequest
import com.teamsparta.abrasax.domain.member.dto.UpdateProfileRequest
import com.teamsparta.abrasax.domain.member.model.Member
import com.teamsparta.abrasax.domain.member.model.toResponse
import com.teamsparta.abrasax.domain.member.repository.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(private val memberRepository: MemberRepository) {

    @Transactional
    fun signUp(request: SignUpRequest): MemberResponse {
        val (email, password, nickname) = request
        val member = Member(
            email = email,
            password = password,
            nickname = nickname,
            // 가입할 때 입력받지 않음
            stringifiedSocialAccounts = "",
        )
        return memberRepository.save(member).toResponse()
    }

    @Transactional
    fun updateProfile(id: Long, request: UpdateProfileRequest): MemberResponse {
        val member =
            memberRepository.findByIdOrNull(id) ?: throw MemberNotFoundException(id)
        val (socialAccounts, nickname) = request
        member.updateProfile(newSocialAccounts = socialAccounts, newNickname = nickname)
        return member.toResponse()
    }

    @Transactional
    fun updatePassword(id: Long, request: UpdatePasswordRequest): MemberResponse {
        val member =
            memberRepository.findByIdOrNull(id) ?: throw MemberNotFoundException(id)
        val (currentPassword, newPassword) = request
        if (member.password != currentPassword) throw PasswordNotMatchException()
        member.updatePassword(newPassword)
        return member.toResponse()
    }
}