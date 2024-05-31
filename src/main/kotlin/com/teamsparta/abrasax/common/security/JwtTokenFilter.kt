package com.teamsparta.abrasax.common.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


// 클라이언트가 서버로 토큰을 보낼때 작동하는 클래스
// Spring Security 에 필요한 구성요소 이기때문에 별도의 서비스에 등록하지 않고 독립적으로 사용
//UsernamePasswordAuthenticationFilter는 Spring Security가 제공하는 인증 필터 중 하나인데 이를 상속한 상태
@Component
class JwtTokenFilter(
    private val jwtTokenProvider: JwtTokenProvider,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = resolveToken(request)
        if (token != null && jwtTokenProvider.validateToken(token)) {
            val auth = jwtTokenProvider.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = auth
        }

        filterChain.doFilter(request, response)
    }


    // 사용자가 토큰과 함께 요청을 보내면은 그 요청에서 토큰을 꺼내는 함수, JWT는 Authorization 이라는 헤더에 토큰 정보가 담겨져있다
    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization") //HTTP 요청의 헤더에서 Authorization 값을 가져와서 bearerToken에다가 저장함
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) { // Bearer로 시작하면은 Bearer를 제거하고 순수 토큰 문자열을 반환
            bearerToken.substring(7)
        } else null
    }
}