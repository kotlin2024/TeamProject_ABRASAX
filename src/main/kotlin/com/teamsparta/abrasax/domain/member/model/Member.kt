package com.teamsparta.abrasax.domain.member.model

import com.teamsparta.abrasax.domain.helper.ListStringifyHelper
import com.teamsparta.abrasax.domain.member.dto.MemberResponse
import jakarta.persistence.*

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
        nickname = newNickname
        stringifiedSocialAccounts = ListStringifyHelper.stringifyList(newSocialAccounts)
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
    }
}

fun Member.toResponse(): MemberResponse {
    return MemberResponse(
        id = id!!,
        nickname = nickname,
        email = email,
        socialAccounts = ListStringifyHelper.parseToList(stringifiedSocialAccounts),
    )
}