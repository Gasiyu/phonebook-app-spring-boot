package org.hyperskill.phonebook.repository

import org.hyperskill.phonebook.model.Employee
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.domain.PageRequest
import java.time.Instant
import java.util.*

@DataJpaTest
internal class EmployeeRepositoryTest {

    @Autowired
    private lateinit var employeeRepository: EmployeeRepository

    @Autowired
    private lateinit var departmentRepository: DepartmentRepository

    @Autowired
    private lateinit var entityManager: TestEntityManager

    private lateinit var testEmployee: Employee

    @BeforeEach
    fun setUp() {
        employeeRepository.deleteAll()
        testEmployee = createTestEmployee()
        entityManager.persistAndFlush(testEmployee)
    }

    @Test
    fun `should save employee successfully`() {
        val newEmployee = createTestEmployee(name = "Jane Smith", position = "Product Manager")

        val savedEmployee = employeeRepository.save(newEmployee)
        entityManager.flush()

        assertAll(
            { assertNotNull(savedEmployee.id) },
            { assertEquals(2, employeeRepository.findAll().size) }
        )
    }

    @Test
    fun `should find employees by department ID`() {
        val pageRequest = PageRequest.of(0, 10)

        val employees = employeeRepository.findByDepartmentId(TEST_DEPARTMENT_ID, pageRequest)

        assertAll(
            { assertEquals(1, employees.content.size) },
            { assertEquals(TEST_EMPLOYEE_NAME, employees.content.first().name) },
            { assertEquals(TEST_DEPARTMENT_ID, employees.content.first().department?.id) }
        )
    }

    @Test
    fun `should find employees by position containing text ignore case`() {
        val pageRequest = PageRequest.of(0, 10)
        val searchTerm = "software"

        val employees = employeeRepository.findByPositionContainingIgnoreCase(searchTerm, pageRequest)

        assertAll(
            { assertEquals(1, employees.content.size) },
            { assertEquals(TEST_EMPLOYEE_NAME, employees.content.first().name) },
            { assertEquals(TEST_POSITION, employees.content.first().position) }
        )
    }

    @Test
    fun `should find employees by department ID and position containing text ignore case`() {
        val pageRequest = PageRequest.of(0, 10)
        val searchTerm = "engineer"

        val employees = employeeRepository.findByDepartmentIdAndPositionContainingIgnoreCase(
            TEST_DEPARTMENT_ID,
            searchTerm,
            pageRequest
        )

        assertAll(
            { assertEquals(1, employees.content.size) },
            { assertEquals(TEST_EMPLOYEE_NAME, employees.content.first().name) },
            { assertEquals(TEST_POSITION, employees.content.first().position) },
            { assertEquals(TEST_DEPARTMENT_ID, employees.content.first().department?.id) }
        )
    }

    @Test
    fun `should return empty page when no employees match department ID`() {
        val nonExistentDepartmentId = UUID.randomUUID()
        val pageRequest = PageRequest.of(0, 10)

        val employees = employeeRepository.findByDepartmentId(nonExistentDepartmentId, pageRequest)

        assertEquals(0, employees.content.size)
    }

    @Test
    fun `should return empty page when no employees match position search`() {
        val nonExistentPosition = "Non Existent Position"
        val pageRequest = PageRequest.of(0, 10)

        val employees = employeeRepository.findByPositionContainingIgnoreCase(nonExistentPosition, pageRequest)

        assertEquals(0, employees.content.size)
    }

    private fun createTestEmployee(
        name: String = TEST_EMPLOYEE_NAME,
        position: String = TEST_POSITION,
        phone: String = TEST_PHONE,
        email: String = TEST_EMAIL
    ): Employee {
        return Employee(
            name = name,
            position = position,
            phone = phone,
            email = email,
            department = departmentRepository.findById(TEST_DEPARTMENT_ID).get(),
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
            isActive = true
        )
    }

    companion object {
        private val TEST_DEPARTMENT_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440001")
        private const val TEST_EMPLOYEE_NAME = "John Doe"
        private const val TEST_POSITION = "Software Engineer"
        private const val TEST_PHONE = "085123123123"
        private const val TEST_EMAIL = "test@gmail.com"
    }
}
