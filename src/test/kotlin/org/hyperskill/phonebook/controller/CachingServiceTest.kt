package org.hyperskill.phonebook.controller

import org.hyperskill.phonebook.model.Department
import org.hyperskill.phonebook.model.Division
import org.hyperskill.phonebook.model.Employee
import org.hyperskill.phonebook.service.EmployeeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.cache.CacheManager
import java.time.Instant
import java.util.*


@SpringBootTest
class CacheIntegrationTest {

    @Autowired
    lateinit var myService: EmployeeService

    @Autowired
    lateinit var cacheManager: CacheManager

    @Test
    fun mockReturn() {
        val userId = "cachePhoneBook"
        val result = myService.getUser(userId)
        val cacheEvict = myService.deleteUser(userId)

        val mockDepartmentId = UUID.fromString("660e8400-e29b-41d4-a716-446655440001")

        val mockDivision = Division(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440001"), name = "Information Technology", isActive = true
        )

        val mockDepartment = Department(
            id = mockDepartmentId,
            name = "Software Development",
            division = mockDivision,
            isActive = true,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )
        val cachePut = myService.putUser(userId = userId, employee = Employee(
            id = UUID.fromString("770e8400-e29b-41d4-a716-446655440001"),
            name = "John Doe",
            position = "Software Engineer",
            phone = "+1-555-0199",
            email = "john.doe@company.com",
            department = mockDepartment,
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
            isActive = true
        ))

        val cachedValue = cacheManager
            .getCache("cachePhoneBook")
            ?.get(userId, String::class.java)

        assertEquals(result, cachedValue)
        assertEquals("User data for $userId", cacheEvict)
        assertEquals("User data for $userId", cachePut)
    }
}
