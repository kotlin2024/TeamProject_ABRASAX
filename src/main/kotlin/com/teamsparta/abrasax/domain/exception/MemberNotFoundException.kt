package com.teamsparta.abrasax.domain.exception

data class MemberNotFoundException(val id: Long) : RuntimeException("Member with id: $id not found")
