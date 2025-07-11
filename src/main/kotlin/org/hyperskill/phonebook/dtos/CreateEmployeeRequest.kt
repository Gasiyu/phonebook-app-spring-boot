package org.hyperskill.phonebook.dtos

import java.util.*

data class CreateEmployeeRequest(
    val name: String,
    val position: String,
    val phone: String,
    val email: String?,
    val departmentId: UUID?
)