package com.teamsparta.abrasax.domain.member.model

import com.teamsparta.abrasax.common.dto.IdResponseDto
import com.teamsparta.abrasax.domain.exception.DomainInvariantException
import com.teamsparta.abrasax.domain.helper.ListStringifyHelper
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.regex.Pattern

@Entity
@Table(name = "member")
class Member(
    @Column(name = "nickname", nullable = false)
    var nickname: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Column(name = "email", nullable = false, unique = true)
    var email: String,

    @Column(name = "social_accounts", nullable = false)
    var stringifiedSocialAccounts: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime,

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime?,

    @Column(name = "social_provider")
    val socialProvider: String?,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun updateProfile(newSocialAccounts: List<String>, newNickname: String) {
        validateNickname(newNickname)
        validateSocialAccounts(newSocialAccounts)
        nickname = newNickname
        stringifiedSocialAccounts = ListStringifyHelper.stringifyList(newSocialAccounts)
        updatedAt = LocalDateTime.now()
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
    }


    companion object {
        private fun validateNickname(newNickname: String) {
            if (newNickname.length > 10) {
                throw DomainInvariantException("Nickname must be less than 10 characters. $newNickname")
            }
            if (!Pattern.matches("^[a-zA-Z0-9가-힣]*$", newNickname)) { //특수문자 확인
                throw DomainInvariantException("Invalid nickname format. $newNickname")
            }
        }

        private fun validateEmail(newEmail: String) {

            if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", newEmail)) {
                throw DomainInvariantException("Invalid email format. $newEmail")
            }
        }


        private fun validateSocialAccounts(newSocialAccounts: List<String>) {
            if (!newSocialAccounts.any { it.startsWith("http://") || it.startsWith("https://") }) {
                throw DomainInvariantException("Social account URLs must start with http:// or https://")
            }

        }


        fun of(email: String, nickname: String, password: String, socialProvider: String?): Member {
            validateNickname(nickname)
            validateEmail(email)

            val timestamp = LocalDateTime.now()
            return Member(
                email = email,
                nickname = nickname,
                password = password,
                stringifiedSocialAccounts = "",
                createdAt = timestamp,
                updatedAt = timestamp,
                deletedAt = null,
                socialProvider = socialProvider
            )
        }
    }
}

fun Member.toIdResponseDto(): IdResponseDto {
    return IdResponseDto(
        id = id!!,
    )
}