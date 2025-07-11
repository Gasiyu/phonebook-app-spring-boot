package org.hyperskill.phonebook.dtos

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.util.*

data class CreateEmployeeRequest(
    @field:NotBlank
    val name: String,

    @field:NotBlank
    val position: String,

    @field:NotBlank
    val phone: String,

    @field:Email
    val email: String?,

    val departmentId: UUID?
)
