package com.teamsparta.abrasax.common.security


import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.util.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetailsService


@Component
class JwtTokenProvider(
    //yml파일에 있는 정보 가져다 쓰기
    @Value("\${jwt.secret}") private val secretKey: String,
    @Value("\${jwt.validity}") private val validityInMilliseconds: Long,
    private val userDetailsService: UserDetailsService // UserDetailService는 Spring Security에서 제공하는 인터페이스(사용자의 상세 정보를 로드하는데 사용됨)
) {

    //토큰을 생성하는 함수
    fun generateToken(email: String): String {
        val now = Date()
        val validity = Date(now.time + validityInMilliseconds)

        return Jwts.builder() //구성요소 설정
            .setSubject(email) // 이메일이 토큰의 식별자로서 사용
            .setIssuedAt(now) // 토큰 발행 시간(현재시간)
            .setExpiration(validity) // 토큰 만료시간 현재 1시간으로 뒤로 설정되어있음
            .signWith(SignatureAlgorithm.HS256, secretKey) // secretKey를 이용해서 토큰에 서명을 해서 무결성을 보장, HS256은 대칭 키 알고리즘
            .compact() // 최종적으로 이 구성들로 토큰을 생성하고 문자열 형태로 반환함
    }


    // 이 토큰이 유효한지 검증하는 함수
    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token) //시크릿 키로 토큰의 서명을 검증하고 토큰을 파싱해서 해당 토큰의 클레임(정보)를 추출함 ,파싱은 토큰에 포함된 정보를 열어보는것
            true // 예외가 발생하지 않으면 이 토큰은 유효한 토큰이므로 true 반환
        } catch (e: Exception) {
            false // 예외발생했으므로 이 토큰은 유효하지 않은 토큰이니 false 반환
        }
    }

    //이 토큰에서 이메일을 추출하는 함수 **요 함수는 AuthService의  login함수에 이미 정의되있으므로 중복된 기능이다  삭제?**
    fun getEmailFromToken(token: String): String {
        val claims: Claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body // 토큰을 파싱하고(열고) 본문인 body를 가져옴
        return claims.subject // 그 body중에 email이 들어있는 subject를 반환함
    }


    // 이 토큰을 사용해서 사용자의 인증 객체를 생성하는 함수, 이 함수는 인가 부분에 사용되는 함수
    fun getAuthentication(token: String): Authentication {
        val email = getEmailFromToken(token) // 토큰에서 이메일 추출한 값을 email변수 안에 저장
        val userDetails = userDetailsService.loadUserByUsername(email) // UserDetailService 인터페이스에 loadUserByUsername()이라는 함수를 사용해서 이메일주소에 해당하는 사용자의 상세 정보를 데이터베이스에서 가져온다???
        val authorities = userDetails.authorities.map { SimpleGrantedAuthority(it.authority) } // 이메일을 통해 가져온 사용자의 정보에서 사용자의권한 정보를 가져와서 SimpleGrantedAuthority 객체로 변환한다. SimpleGrantedAuthority 객체는 문자열 형태의 권한 이름을 가지고 있다 예시로 ADMIN이나 NORMAL같은 ROLE의 형태?
        return UsernamePasswordAuthenticationToken(userDetails, "",authorities) // 사용자 상세 정보랑 권한 정보를 사용해서 사용자의 인증 정보를 나타내는 UsernamePasswordAuthenticationToken 객체를 생성해서 반환함
    }
}