package org.hyperskill.phonebook.dtos

data class JwtTokenResult(
    val token: String,
    val expiresAt: Long
)
