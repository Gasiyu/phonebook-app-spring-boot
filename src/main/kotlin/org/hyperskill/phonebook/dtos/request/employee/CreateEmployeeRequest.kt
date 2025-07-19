package org.hyperskill.phonebook.dtos.request.employee

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.util.*

data class CreateEmployeeRequest(
    @field:NotBlank(message = "Name must not be blank")
    val name: String? = null,

    @field:NotBlank(message = "Position must not be blank")
    val position: String? = null,

    @field:NotBlank(message = "Phone must not be blank")
    val phone: String? = null,

    @field:Email(message = "Email must be a valid email address")
    val email: String? = null,

    val departmentId: UUID? = null
)
