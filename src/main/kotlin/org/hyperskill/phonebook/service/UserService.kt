package org.hyperskill.phonebook.service

import org.hyperskill.phonebook.dtos.UserDto
import org.hyperskill.phonebook.exception.NotFoundException
import org.hyperskill.phonebook.model.User
import org.hyperskill.phonebook.model.Role
import org.hyperskill.phonebook.repository.RoleRepository
import org.hyperskill.phonebook.repository.UserRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import java.util.UUID


@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
) {
    private fun toUserDto(user: User): UserDto {
        return UserDto(
            id = user.id!!,
            username = user.username,
            isActive = user.isActive,
            roles = user.roles.toSet(),
        )
    }

    fun getRoles(): List<Role> {
        return roleRepository.findAll().toList()
    }

    fun getRole(id: UUID): Role {
        return roleRepository.findById(id).orElseThrow { NotFoundException("Role with $id not found") }
    }

    fun getUser(id: UUID): User {
        return userRepository.findById(id).orElseThrow { NotFoundException("User with $id not found") }
    }

    fun addRole(userId: UUID, roleId: UUID): UserDto {
        val user = getUser(userId)
        val role = getRole(roleId)
        user.roles.add(role)
        userRepository.save(user)

        return user.run(::toUserDto)
    }

    fun removeRole(userId: UUID, roleId: UUID): UserDto {
        val user = getUser(userId)
        user.roles.removeIf { it.id == roleId }
        userRepository.save(user)

        return user.run(::toUserDto)
    }

    @Cacheable("cachePhoneBook")
    fun getUser(@PathVariable userId: String): String {
        println("Fetching from DB...")
        return "User data for $userId"
    }

    @CacheEvict("cachePhoneBook", key = "#userId")
    fun deleteUser(@PathVariable userId: String): String {
        println("Evicting cache...")
        return  "User data for $userId"
    }
}