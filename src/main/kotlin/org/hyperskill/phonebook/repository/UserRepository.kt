package org.hyperskill.phonebook.repository

import org.hyperskill.phonebook.model.User
import org.springframework.data.repository.CrudRepository
import java.util.Optional
import java.util.UUID

interface UserRepository : CrudRepository<User, UUID> {
    fun findByUsername(username: String): Optional<User>
}