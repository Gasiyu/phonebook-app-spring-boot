package org.hyperskill.phonebook.dtos

import jakarta.validation.constraints.NotBlank
import java.util.UUID

class AddUserRoleRequest(
    @NotBlank(message = "Please provide role id") val roleId: UUID
)