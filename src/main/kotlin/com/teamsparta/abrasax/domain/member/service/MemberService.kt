package com.teamsparta.abrasax.domain.member.service

import com.teamsparta.abrasax.domain.exception.MemberNotFoundException
import com.teamsparta.abrasax.domain.member.dto.MemberResponse
import com.teamsparta.abrasax.domain.member.dto.UpdateProfileRequest
import com.teamsparta.abrasax.domain.member.model.toResponse
import com.teamsparta.abrasax.domain.member.repository.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(private val memberRepository: MemberRepository) {


    @Transactional
    fun updateProfile(id: Long, request: UpdateProfileRequest): MemberResponse {
        val member =
            memberRepository.findByIdOrNull(id) ?: throw MemberNotFoundException(id)
        val (socialAccounts, nickname) = request
        member.updateProfile(newSocialAccounts = socialAccounts, newNickname = nickname)
        return member.toResponse()
    }


}