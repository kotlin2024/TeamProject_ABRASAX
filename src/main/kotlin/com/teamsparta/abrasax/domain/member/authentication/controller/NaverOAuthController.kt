package com.teamsparta.abrasax.domain.member.authentication.controller

import com.teamsparta.abrasax.domain.member.authentication.dto.LoginRequest
import com.teamsparta.abrasax.domain.member.authentication.dto.LoginResponse
import com.teamsparta.abrasax.domain.member.authentication.dto.OAuthSignUpRequest
import com.teamsparta.abrasax.domain.member.authentication.service.NaverOAuthServiceImpl
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RequestMapping("/oauth2")
@RestController
class NaverOAuthController(
    private val naverOAuthServiceImpl: NaverOAuthServiceImpl,
) {
    @GetMapping("/naver/login")
    fun login(): ModelAndView {
        val url = naverOAuthServiceImpl.getNaverAuthorizeURl("authorize")

        return ModelAndView("redirect:$url")
    }

    @Operation(hidden = true)
    @GetMapping("/naver/success")
    fun loginSuccess(
        @RequestParam state: String,
        @RequestParam code: String?,
        @RequestParam error: String?,
        @RequestParam(name = "error_description") errorDescription: String?,
    ): LoginResponse {
        if (error != null) throw IllegalStateException(errorDescription!!)
        val tokenResponse = naverOAuthServiceImpl.getNaverTokenResponse("token", code!!)

        val (id, email, _, nickname) = naverOAuthServiceImpl.getNaverUserData(tokenResponse.accessToken)
        val password = id.substring(0 until 20)
        if (naverOAuthServiceImpl.checkSignedUp(email) == false) {
            naverOAuthServiceImpl.signUp(
                OAuthSignUpRequest(
                    email = email,
                    password = password,
                    nickname = nickname,
                    socialProvider = "Naver"
                )
            )
        }
        return naverOAuthServiceImpl.login(LoginRequest(email, password))
    }
}
