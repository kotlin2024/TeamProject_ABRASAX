package com.teamsparta.abrasax.domain.member.authentication.service


import com.teamsparta.abrasax.domain.member.repository.MemberRepository

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service //@service어노테이션이 있지만 컨트롤러에서 사용되는게 아니라 Spring Security에서 사용자 인증과 권한 부여 과정에서 내부적으로 사용된다
class GetUserDetailsService(
    private val memberRepository: MemberRepository,
) : UserDetailsService { // UserDetailsService 요녀석이 데이터베이스에서 사용자 정보를 가져오는 서비스

    override fun loadUserByUsername(email: String): UserDetails { // 오버라이드를 통해서 내가 원하는 데이터베이스로 연결
        val user = memberRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("해당 이메일이 존재하지 않음")

        return User(
            user.email,
            user.password,
            emptyList()
        )
    }
}