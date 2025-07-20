package org.hyperskill.phonebook.dtos.request.auth

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank(message = "Username is required and cannot be blank")
    val username: String? = null,
    @field:NotBlank(message = "Password is required and cannot be blank")
    val password: String? = null
)
