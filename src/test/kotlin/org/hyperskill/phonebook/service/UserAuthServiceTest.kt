package org.hyperskill.phonebook.service

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hyperskill.phonebook.model.User
import org.hyperskill.phonebook.repository.UserRepository
import java.util.Optional
import java.util.UUID

class UserAuthServiceTest : WordSpec({

  val userRepository = mockk<UserRepository>()
    val userAuthService = UserAuthService(userRepository)

    val user = User(
        id = UUID.randomUUID(),
        username = "user",
        password = "password",
        isActive = true,
        roles = mutableSetOf()
    )

    "UserAuthService" should {
        "load user by username" {
            every { userRepository.findByUsername(user.username) } returns Optional.of(user)

            val userDetails = userAuthService.loadUserByUsername(user.username)

            userDetails.username shouldBe user.username
            userDetails.isEnabled shouldBe true

            verify { userRepository.findByUsername(user.username) }
        }

    }

})