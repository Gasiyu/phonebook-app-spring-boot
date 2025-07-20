package org.hyperskill.phonebook.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.hyperskill.phonebook.exception.NotFoundException
import org.hyperskill.phonebook.model.Role
import org.hyperskill.phonebook.repository.RoleRepository
import org.hyperskill.phonebook.repository.UserRepository
import java.util.*

class UserServiceTest : WordSpec({

    val userRepository = mockk<UserRepository>(relaxed = true)
    val roleRepository = mockk<RoleRepository>(relaxed = true)
    val userService = UserService(userRepository, roleRepository)

    "userService" should {

        "getRoles returns all roles" {
            val roles = listOf(
                Role(UUID.randomUUID(), "ADMIN"),
                Role(UUID.randomUUID(), "USER")
            )
            every { roleRepository.findAll() } returns roles

            userService.getRoles() shouldBe roles
        }

        "getRole returns role when found" {
            val roleId = UUID.randomUUID()
            val role = Role(roleId, "ADMIN")
            every { roleRepository.findById(roleId) } returns Optional.of(role)

            userService.getRole(roleId) shouldBe role
        }

        "getRole throws NotFoundException when not found" {
            val roleId = UUID.randomUUID()
            every { roleRepository.findById(roleId) } returns Optional.empty()

            shouldThrow<NotFoundException> {
                userService.getRole(roleId)
            }
        }


        "getUser throws NotFoundException when not found" {
            val userId = UUID.randomUUID()
            every { userRepository.findById(userId) } returns Optional.empty()

            shouldThrow<NotFoundException> {
                userService.getUser(userId)
            }
        }

    }


})