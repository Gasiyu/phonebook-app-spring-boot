package org.hyperskill.phonebook.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.hyperskill.phonebook.dtos.CreateEmployeeRequest
import org.hyperskill.phonebook.model.Department
import org.hyperskill.phonebook.model.Division
import org.hyperskill.phonebook.model.Employee
import org.hyperskill.phonebook.service.EmployeeServices
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.Instant
import java.util.*

@WebMvcTest(EmployeeController::class)
internal class EmployeeControllerTest(
    @Autowired private val mockMvc: MockMvc
) {

    @field:MockitoBean
    private lateinit var employeeServices: EmployeeServices

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val mockDepartmentId = UUID.fromString("660e8400-e29b-41d4-a716-446655440001")

    private val mockDivision = Division(
        id = UUID.fromString("550e8400-e29b-41d4-a716-446655440001"), name = "Information Technology", isActive = true
    )

    private val mockDepartment = Department(
        id = mockDepartmentId,
        name = "Software Development",
        division = mockDivision,
        isActive = true,
        createdAt = Instant.now(),
        updatedAt = Instant.now()
    )

    private val mockCreateEmployeeRequest = CreateEmployeeRequest(
        name = "John Doe",
        position = "Software Engineer",
        phone = "+1-555-0199",
        email = "john.doe@company.com",
        departmentId = mockDepartmentId
    )

    private val mockEmployee = Employee(
        id = UUID.fromString("770e8400-e29b-41d4-a716-446655440001"),
        name = "John Doe",
        position = "Software Engineer",
        phone = "+1-555-0199",
        email = "john.doe@company.com",
        department = mockDepartment,
        createdAt = Instant.now(),
        updatedAt = Instant.now(),
        isActive = true
    )

    @Test
    @Throws(Exception::class)
    fun `POST employees with valid request returns valid ResponseEntity`() {
        `when`(employeeServices.store(mockCreateEmployeeRequest)).thenReturn(mockEmployee)

        val requestBuilder = post("/api/employees").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(mockCreateEmployeeRequest))

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isCreated,
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.id").value(mockEmployee.id.toString()),
                jsonPath("$.name").value("John Doe"),
                jsonPath("$.position").value("Software Engineer"),
                jsonPath("$.phone").value("+1-555-0199"),
                jsonPath("$.email").value("john.doe@company.com"),
                jsonPath("$.active").value(true)
            )
    }

    @Test
    @Throws(Exception::class)
    fun `POST employees with minimal valid request returns created`() {
        val requestBuilder = post("/api/employees").contentType(MediaType.APPLICATION_JSON).content(
                """
                {
                  "name": "test",
                  "position": "test",
                  "phone": "test"
                }
            """.trimIndent()
            )

        mockMvc.perform(requestBuilder).andExpect(status().isCreated)
    }

    @Test
    @Throws(Exception::class)
    fun `POST employees with invalid request returns bad request`() {
        val requestBuilder = post("/api/employees").contentType(MediaType.APPLICATION_JSON).content(
                """
                {
                  "name": "test"
                }
            """.trimIndent()
            )

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest)
    }

    @Test
    @Throws(Exception::class)
    fun `POST employees with invalid email returns bad request`() {
        val requestBuilder = post("/api/employees").contentType(MediaType.APPLICATION_JSON).content(
                """
                {
                  "name": "test",
                  "position": "test",
                  "phone": "test",
                  "email": "test"
                }
            """.trimIndent()
            )

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest)
    }

    @Test
    @Throws(Exception::class)
    fun `POST employees with invalid uuid returns bad request`() {
        val requestBuilder = post("/api/employees").contentType(MediaType.APPLICATION_JSON).content(
                """
                {
                  "name": "test",
                  "position": "test",
                  "phone": "test",
                  "departmentId": "this-is-not-uuid"
                }
            """.trimIndent()
            )

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest)
    }
}
