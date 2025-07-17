package org.hyperskill.phonebook.dtos

import org.hyperskill.phonebook.model.Role
import java.util.UUID

class UserDto(
    var id: UUID,
    var username: String,
    var isActive: Boolean,
    var roles: Set<Role>
)