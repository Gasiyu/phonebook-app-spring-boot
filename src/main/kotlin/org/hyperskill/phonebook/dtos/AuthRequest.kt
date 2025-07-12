package org.hyperskill.phonebook.dtos

data class AuthRequest(
    val username: String,
    val password: String
)