package com.teamsparta.abrasax.domain.member.model

import com.teamsparta.abrasax.domain.helper.ListStringifyHelper
import com.teamsparta.abrasax.domain.member.dto.MemberResponse
import jakarta.persistence.*
import java.util.regex.Pattern

@Entity
@Table(name = "member")
class Member(
    @Column(name = "nickname")
    var nickname: String,

    @Column(name = "password")
    var password: String,

    @Column(name = "email")
    var email: String,

    @Column(name = "social_accounts")
    var stringifiedSocialAccounts: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun updateProfile(newSocialAccounts: List<String>, newNickname: String) {
        validateNickname(nickname)
        validateSocialAccounts(newSocialAccounts)
        nickname = newNickname
        stringifiedSocialAccounts = ListStringifyHelper.stringifyList(newSocialAccounts)


    }

    fun updatePassword(newPassword: String) {
        password = newPassword
    }


    companion object {
        private fun validateNickname(nickname: String) {
            if (nickname.length > 10) {
                throw IllegalArgumentException("Nickname must be less than 10 characters.")
            }
            if (!Pattern.matches("^[a-zA-Z0-9가-힣]*$", nickname)) { //특수문자 확인
                throw IllegalArgumentException("Invalid nickname format.")
            }
        }

        private fun validateEmail(email: String) {

            if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", email)) {
                throw IllegalArgumentException("Invalid email format.")
            }
        }


        private fun validateSocialAccounts(socialAccounts: List<String>) {
            if (!socialAccounts.all { it.startsWith("http://") } || !socialAccounts.all { it.startsWith("https://") }) {
                throw IllegalArgumentException("Social account URLs must start with http:// or https://")
            }

        }


        fun of(email: String, nickname: String, password: String): Member {
            validateNickname(nickname)
            validateEmail(email)


            return Member(
                email = email,
                nickname = nickname,
                password = password,
                stringifiedSocialAccounts = ""
            )
        }
    }
}

fun Member.toResponse(): MemberResponse {
    return MemberResponse(
        id = id!!,
    )
}