package org.hyperskill.phonebook.dtos.response.auth

class LoginResponse(
    val accessToken: String,
    val expiresInSeconds: Long,
    val expiresAt: Long
)
