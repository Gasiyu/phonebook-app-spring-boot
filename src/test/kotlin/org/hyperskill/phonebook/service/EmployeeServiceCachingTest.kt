package org.hyperskill.phonebook.service

import org.hyperskill.phonebook.dtos.request.employee.CreateEmployeeRequest
import org.hyperskill.phonebook.dtos.request.employee.UpdateEmployeeRequest
import org.hyperskill.phonebook.model.Department
import org.hyperskill.phonebook.model.Division
import org.hyperskill.phonebook.model.Employee
import org.hyperskill.phonebook.repository.EmployeeRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.anyOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.CacheManager
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*

@SpringBootTest
class EmployeeServiceCachingTest {

    @Autowired
    lateinit var employeeService: EmployeeService

    @Autowired
    lateinit var cacheManager: CacheManager

    @MockitoBean
    lateinit var employeeRepository: EmployeeRepository

    private lateinit var mockEmployee: Employee
    private lateinit var mockEmployeeId: UUID

    @BeforeEach
    fun setup() {
        // Clear the cache before each test
        cacheManager.getCache("employees")?.clear()

        // Create mock data
        mockEmployeeId = UUID.randomUUID()
        val mockDivision = Division(
            id = UUID.randomUUID(),
            name = "Information Technology",
            isActive = true
        )
        val mockDepartment = Department(
            id = UUID.randomUUID(),
            name = "Software Development",
            division = mockDivision,
            isActive = true,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )
        mockEmployee = Employee(
            id = mockEmployeeId,
            name = "John Doe",
            position = "Software Engineer",
            phone = "+1-555-0199",
            email = "john.doe@company.com",
            department = mockDepartment,
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
            isActive = true
        )

        // Setup repository mock
        doReturn(Optional.of(mockEmployee)).`when`(employeeRepository).findById(mockEmployeeId)
        doReturn(mockEmployee).`when`(employeeRepository).save(anyOrNull())

        val pageRequest = PageRequest.of(0, 10)
        val employeePage = PageImpl(listOf(mockEmployee), pageRequest, 1)
        doReturn(employeePage).`when`(employeeRepository).findAll(any(PageRequest::class.java))
    }

    @AfterEach
    fun tearDown() {
        // Clear the cache after each test
        cacheManager.getCache("employees")?.clear()
    }

    @Test
    fun `test getEmployee cache hit`() {
        // First call should hit the repository
        val result1 = employeeService.getEmployee(mockEmployeeId)

        // Second call should hit the cache
        val result2 = employeeService.getEmployee(mockEmployeeId)

        // Verify both results are the same
        assertEquals(result1, result2)
        assertEquals(mockEmployee, result1)

        // Verify the value is in the cache
        val cachedValue = cacheManager.getCache("employees")?.get(mockEmployeeId, Employee::class.java)
        assertNotNull(cachedValue)
        assertEquals(mockEmployee, cachedValue)
    }

    @Test
    fun `test store method updates cache`() {
        // Create a new employee
        val createRequest = CreateEmployeeRequest(
            name = "Jane Smith",
            position = "Product Manager",
            phone = "+1-555-0200",
            email = "jane.smith@company.com",
            departmentId = null
        )

        // Mock the save method to return our mock employee
        `when`(employeeRepository.save(anyOrNull())).thenReturn(mockEmployee)

        // Call the store method
        val result = employeeService.store(createRequest)

        // Verify the result is our mock employee
        assertEquals(mockEmployee, result)

        // Verify the value is not in the cache
        val evictedEmployee = cacheManager.getCache("employees")?.get(mockEmployeeId, Employee::class.java)
        assertNull(evictedEmployee)

        // Verify the value is in the cache
        employeeService.getEmployee(result.id!!)
        val cachedValue = cacheManager.getCache("employees")?.get(result.id!!, Employee::class.java)
        assertNotNull(cachedValue)
    }

    @Test
    fun `test updateEmployee updates cache`() {
        // Create an update request
        val updateRequest = UpdateEmployeeRequest(
            name = "John Doe Updated",
            position = "Senior Software Engineer",
            phone = "+1-555-0199",
            email = "john.doe@company.com",
            departmentId = null
        )

        // Call the updateEmployee method
        val result = employeeService.updateEmployee(mockEmployeeId, updateRequest)
        employeeService.getEmployee(mockEmployeeId)

        // Verify the result is our mock employee
        assertEquals(mockEmployee, result)

        // Verify the value is in the cache
        val cachedValue = cacheManager.getCache("employees")?.get(mockEmployeeId, Employee::class.java)
        assertNotNull(cachedValue)
        assertEquals(mockEmployee, cachedValue)
    }

    @Test
    fun `test deleteEmployee removes from cache`() {
        // First put the employee in the cache
        employeeService.getEmployee(mockEmployeeId)

        // Verify it's in the cache
        assertNotNull(cacheManager.getCache("employees")?.get(mockEmployeeId))

        // Delete the employee
        employeeService.deleteEmployee(mockEmployeeId)

        // Verify it's no longer in the cache
        assertNull(cacheManager.getCache("employees")?.get(mockEmployeeId))
    }

    @Test
    fun `test getAllEmployees uses cache`() {
        // First call should hit the repository
        val result1 = employeeService.getAllEmployees(0)

        // Second call should hit the cache
        val result2 = employeeService.getAllEmployees(0)

        // Verify both results are the same
        assertEquals(result1, result2)

        // Verify the value is in the cache
        val cachedValue = cacheManager.getCache("employees")?.get("all-page-0")
        assertNotNull(cachedValue)
    }
}
