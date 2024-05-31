package com.teamsparta.abrasax.common.security

import com.teamsparta.abrasax.domain.member.authentication.service.GetUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val getUserDetailsService: GetUserDetailsService,
    private val jwtTokenFilter: JwtTokenFilter
) {
    private val allowedUrls = arrayOf(
        "/",
        "/swagger-ui/**",
        "/v3/**",
        "/auth/**",
        "/oauth2/**",
    )

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .headers { it.frameOptions { frameOptions -> frameOptions.sameOrigin() } }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.requestMatchers(*allowedUrls).permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtTokenFilter, BasicAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) { //AuthenticationManagerBuilder 는 사용자 인증 정보를 담고있는 빌더 클래스
        auth.userDetailsService(getUserDetailsService) //userDetailsService 는 데이터베이스에 있는 정보를 가져옴, .passwordEncoder(passwordEncoder) 요 부분이 사용자가 입력한 비밀번호랑 데이터베이스에 있는 비밀번호랑 맞는지의 로직
    } //userDetailService 를 구현하고 있는것이 GetUserDetailsService 임
    //요 함수는 AuthenticationManagerBuilder 를 사용해서 사용자 인증 정보를 전역적으로 구성하는 함수다
}