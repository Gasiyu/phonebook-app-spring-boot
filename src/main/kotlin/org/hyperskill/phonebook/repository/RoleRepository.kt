package org.hyperskill.phonebook.repository

import org.hyperskill.phonebook.model.Role
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface RoleRepository: CrudRepository<Role, UUID>
