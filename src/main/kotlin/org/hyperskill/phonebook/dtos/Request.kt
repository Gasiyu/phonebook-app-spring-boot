package org.hyperskill.phonebook.dtos


import jakarta.validation.constraints.NotBlank

class Request (
    @field:NotBlank
    val username: String,
    @field:NotBlank
    val password: String,
)