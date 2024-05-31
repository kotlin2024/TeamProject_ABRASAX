package com.teamsparta.abrasax.domain.member.authentication.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.teamsparta.abrasax.common.security.JwtTokenProvider
import com.teamsparta.abrasax.common.security.NaverOauthProperties
import com.teamsparta.abrasax.domain.member.authentication.dto.*
import com.teamsparta.abrasax.domain.member.dto.MemberResponse
import com.teamsparta.abrasax.domain.member.model.Member
import com.teamsparta.abrasax.domain.member.model.toResponse
import com.teamsparta.abrasax.domain.member.repository.MemberRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
class NaverOAuthServiceImpl(
    private val naverOauthProperties: NaverOauthProperties,
    private val memberRepository: MemberRepository,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder,
) : OAuthService {

    override fun login(request: LoginRequest): LoginResponse {
        val (email, password) = request
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(email, password)
        )
        return LoginResponse(token = jwtTokenProvider.generateToken(email))
    }

    override fun signUp(request: OAuthSignUpRequest): MemberResponse {
        val (email, password, nickname, socialProvider) = request
        val member = Member.of(
            email = email,
            password = passwordEncoder.encode(password),
            nickname = nickname,
            socialProvider = socialProvider
        )
        return memberRepository.save(member).toResponse()
    }

    fun getNaverAuthorizeURl(type: String): String {
        val baseUrl = naverOauthProperties.baseUrl
        val clientId = naverOauthProperties.clientId
        val redirectUri = naverOauthProperties.redirectUri

        val uriComponents = UriComponentsBuilder.fromUriString("${baseUrl}/${type}")
            .queryParam("response_type", "code")
            .queryParam("client_id", clientId)
            .queryParam("redirect_uri", URLEncoder.encode(redirectUri, "UTF-8"))
            .queryParam("state", URLEncoder.encode("TEST", "UTF-8"))
            .build()
        return uriComponents.toString()
    }

    fun getNaverTokenResponse(type: String, code: String): NaverTokenResponse {
        val baseUrl = naverOauthProperties.baseUrl
        val clientId = naverOauthProperties.clientId
        val clientSecret = naverOauthProperties.clientSecret

        val uriComponents = UriComponentsBuilder.fromUriString("${baseUrl}/${type}")
            .queryParam("client_id", clientId)
            .queryParam("client_secret", clientSecret)
            .queryParam("grant_type", "authorization_code")
            .queryParam("state", URLEncoder.encode("TEST", "UTF-8"))
            .queryParam("code", code)
            .build()
        val url = uriComponents.toUriString()
        val request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build()
        val response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString())

        return jacksonObjectMapper().readValue<NaverTokenResponse>(response.body())
    }

    fun getNaverUserData(token: String): NaverUserData {
        val url = "https://openapi.naver.com/v1/nid/me"
        val request =
            HttpRequest.newBuilder().header("Authorization", "Bearer $token").uri(URI.create(url)).GET().build()
        val response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString())

        return jacksonObjectMapper().readValue<NaverUserDataResponse>(response.body()).response
    }

    fun checkSignedUp(email: String): Boolean {
        return memberRepository.existsByEmail(email)
    }
}
