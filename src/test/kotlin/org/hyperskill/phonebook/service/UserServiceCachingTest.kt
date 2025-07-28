package org.hyperskill.phonebook.service

import org.hyperskill.phonebook.dtos.UserDto
import org.hyperskill.phonebook.model.Role
import org.hyperskill.phonebook.model.User
import org.hyperskill.phonebook.repository.RoleRepository
import org.hyperskill.phonebook.repository.UserRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.CacheManager
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*

@SpringBootTest
class UserServiceCachingTest {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var cacheManager: CacheManager

    @MockitoBean
    lateinit var userRepository: UserRepository

    @MockitoBean
    lateinit var roleRepository: RoleRepository

    private lateinit var mockUser: User
    private lateinit var mockUserId: UUID
    private lateinit var mockRole: Role
    private lateinit var mockRoleId: UUID

    @BeforeEach
    fun setup() {
        // Clear the cache before each test
        cacheManager.getCache("users")?.clear()

        // Create mock data
        mockUserId = UUID.randomUUID()
        mockUser = User(
            id = mockUserId,
            username = "testuser",
            password = "password",
            isActive = true,
            roles = mutableSetOf()
        )

        mockRoleId = UUID.randomUUID()
        mockRole = Role(
            id = mockRoleId,
            name = "ROLE_USER"
        )

        // Setup repository mocks
        `when`(userRepository.findById(mockUserId)).thenReturn(Optional.of(mockUser))
        `when`(userRepository.save(anyOrNull())).thenReturn(mockUser)

        `when`(roleRepository.findById(mockRoleId)).thenReturn(Optional.of(mockRole))
        `when`(roleRepository.findAll()).thenReturn(listOf(mockRole))
    }

    @AfterEach
    fun tearDown() {
        // Clear the cache after each test
        cacheManager.getCache("users")?.clear()
    }

    @Test
    fun `test getUser cache hit`() {
        // First call should hit the repository
        val result1 = userService.getUser(mockUserId)

        // Second call should hit the cache
        val result2 = userService.getUser(mockUserId)

        // Verify both results are the same
        assertEquals(result1, result2)
        assertEquals(mockUser, result1)

        // Verify the value is in the cache
        val cachedValue = cacheManager.getCache("users")?.get(mockUserId, User::class.java)
        assertNotNull(cachedValue)
        assertEquals(mockUser, cachedValue)
    }

    @Test
    fun `test getRoles cache hit`() {
        // First call should hit the repository
        val result1 = userService.getRoles()

        // Second call should hit the cache
        val result2 = userService.getRoles()

        // Verify both results are the same
        assertEquals(result1, result2)

        // Verify the value is in the cache
        val cachedValue = cacheManager.getCache("users")?.get("all-roles")
        assertNotNull(cachedValue)
    }

    @Test
    fun `test getRole cache hit`() {
        // First call should hit the repository
        val result1 = userService.getRole(mockRoleId)

        // Second call should hit the cache
        val result2 = userService.getRole(mockRoleId)

        // Verify both results are the same
        assertEquals(result1, result2)
        assertEquals(mockRole, result1)

        // Verify the value is in the cache
        val cachedValue = cacheManager.getCache("users")?.get("role-" + mockRoleId, Role::class.java)
        assertNotNull(cachedValue)
        assertEquals(mockRole, cachedValue)
    }

    @Test
    fun `test addRole updates user cache and evicts userDto cache`() {
        // First put the user in the cache
        userService.getUser(mockUserId)

        // Verify it's in the cache
        assertNotNull(cacheManager.getCache("users")?.get(mockUserId))

        // Add a role
        userService.addRole(mockUserId, mockRoleId)

        // Verify the user is still in the cache (updated)
        val cachedUser = cacheManager.getCache("users")?.get(mockUserId, UserDto::class.java)
        assertNotNull(cachedUser)

        // Verify the userDto cache entry was evicted
        assertNull(cacheManager.getCache("users")?.get("user-dto-" + mockUserId))
    }

    @Test
    fun `test removeRole updates user cache and evicts userDto cache`() {
        // Add the role to the user first
        mockUser.roles.add(mockRole)

        // First put the user in the cache
        userService.getUser(mockUserId)

        // Verify it's in the cache
        assertNotNull(cacheManager.getCache("users")?.get(mockUserId))

        // Remove the role
        userService.removeRole(mockUserId, mockRoleId)

        // Verify the user is still in the cache (updated)
        val cachedUser = cacheManager.getCache("users")?.get(mockUserId, UserDto::class.java)
        assertNotNull(cachedUser)

        // Verify the userDto cache entry was evicted
        assertNull(cacheManager.getCache("users")?.get("user-dto-" + mockUserId))
    }
}
