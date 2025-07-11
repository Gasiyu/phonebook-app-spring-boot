package org.hyperskill.phonebook.dtos

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.util.*

data class CreateEmployeeRequest(
    @field:NotBlank(message = "Name must not be blank")
    val name: String,

    @field:NotBlank(message = "Position must not be blank")
    val position: String,

    @field:NotBlank(message = "Phone must not be blank")
    val phone: String,

    @field:Email(message = "Email must be a valid email address")
    val email: String?,

    val departmentId: UUID?
)
