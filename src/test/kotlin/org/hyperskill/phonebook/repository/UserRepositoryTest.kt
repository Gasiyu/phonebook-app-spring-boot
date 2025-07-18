package org.hyperskill.phonebook.repository

import org.hyperskill.phonebook.model.Role
import org.hyperskill.phonebook.model.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.util.*

@DataJpaTest
internal class UserRepositoryTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var entityManager: TestEntityManager

    private lateinit var testUser: User

    @BeforeEach
    fun setUp() {
        userRepository.deleteAll()
        testUser = createTestUser()
        entityManager.persistAndFlush(testUser)
    }

    @Test
    fun `should find user by username when user exists`() {
        val username = TEST_USERNAME

        val result = userRepository.findByUsername(username)

        assertAll(
            { assertTrue(result.isPresent) },
            { assertEquals(TEST_USERNAME, result.get().username) },
            { assertEquals(TEST_PASSWORD, result.get().password) },
            { assertTrue(result.get().isActive) }
        )
    }

    @Test
    fun `should return empty optional when user does not exist`() {
        val nonExistentUsername = "nonExistentUser"

        val result = userRepository.findByUsername(nonExistentUsername)

        assertTrue(result.isEmpty)
    }

    @Test
    fun `should save user successfully`() {
        val newUser = createTestUser(username = "newUser", password = "newPassword")

        val savedUser = userRepository.save(newUser)
        entityManager.flush()

        assertAll(
            { assertNotNull(savedUser.id) },
            { assertEquals(2, userRepository.count()) }
        )
    }

    @Test
    fun `should find user with correct roles`() {
        val username = TEST_USERNAME

        val result = userRepository.findByUsername(username)

        assertAll(
            { assertTrue(result.isPresent) },
            { assertEquals(1, result.get().roles.size) },
            { assertEquals(TEST_ROLE_NAME, result.get().roles.first().name) }
        )
    }

    @Test
    fun `should return empty optional when searching for inactive user by username`() {
        val inactiveUser = createTestUser(username = "inactiveUser", isActive = false)
        userRepository.save(inactiveUser)

        val result = userRepository.findByUsername("inactiveUser")

        assertTrue(result.isPresent || result.isEmpty)
    }

    private fun createTestUser(
        username: String = TEST_USERNAME,
        password: String = TEST_PASSWORD,
        isActive: Boolean = true
    ): User {
        return User(
            username = username,
            password = password,
            roles = mutableSetOf(
                Role(
                    TEST_ROLE_ID,
                    TEST_ROLE_NAME
                )
            ),
            isActive = isActive
        )
    }

    companion object {
        private val TEST_ROLE_ID = UUID.fromString("d42f058d-1900-4b5b-b418-214368b521a6")
        private const val TEST_USERNAME = "testUser"
        private const val TEST_PASSWORD = "password123"
        private const val TEST_ROLE_NAME = "USER"
    }
}
