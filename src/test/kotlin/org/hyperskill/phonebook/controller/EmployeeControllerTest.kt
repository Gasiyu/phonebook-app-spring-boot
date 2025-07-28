package org.hyperskill.phonebook.controller

import org.hyperskill.phonebook.SecurityConfig
import org.hyperskill.phonebook.security.CustomAccessDeniedHandler
import org.hyperskill.phonebook.security.CustomAuthenticationEntryPoint
import org.hyperskill.phonebook.service.EmployeeService
import org.hyperskill.phonebook.service.JwtService
import org.hyperskill.phonebook.service.UserAuthService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@WebMvcTest(EmployeeController::class)
@Import(SecurityConfig::class)
class EmployeeControllerTest(
    @Autowired private val mockMvc: MockMvc
) {

    @field:MockitoBean
    private lateinit var employeeService: EmployeeService

    @field:MockitoBean
    private lateinit var jwtService: JwtService

    @field:MockitoBean
    private lateinit var userAuthService: UserAuthService

    @field:MockitoBean
    private lateinit var customAccessDeniedHandler: CustomAccessDeniedHandler

    @field:MockitoBean
    private lateinit var customAuthenticationEntryPoint: CustomAuthenticationEntryPoint

    @Test
    @Throws(Exception::class)
    fun `GET employees without authorization returns 401`() {
        val requestBuilder = get("/api/employees")

        mockMvc.perform(requestBuilder)
            .andExpect(status().isUnauthorized())
    }

    @Test
    @WithMockUser(authorities = ["ROLE_USER"])
    @Throws(Exception::class)
    fun `GET employees with 'ROLE_USER' MockUser returns 200`() {
        val requestBuilder = get("/api/employees")

        mockMvc.perform(requestBuilder)
            .andExpect(status().isOk())
    }

    @Test
    @WithMockUser(authorities = ["ROLE_MANAGER"])
    @Throws(Exception::class)
    fun `GET employees with 'ROLE_MANAGER' MockUser returns 200`() {
        val requestBuilder = get("/api/employees")

        mockMvc.perform(requestBuilder)
            .andExpect(status().isOk())
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    @Throws(Exception::class)
    fun `GET employees with 'ROLE_ADMIN' MockUser returns 200`() {
        val requestBuilder = get("/api/employees")

        mockMvc.perform(requestBuilder)
            .andExpect(status().isOk())
    }

    @Test
    @WithMockUser(authorities = ["ROLE_USER"])
    @Throws(Exception::class)
    fun `GET employees with invalid page parameter returns 400`() {
        val requestBuilder = get("/api/employees")
            .param("page", "-1")

        mockMvc.perform(requestBuilder)
            .andExpect(status().isBadRequest())
    }

    @Test
    @WithMockUser(authorities = ["ROLE_USER"])
    @Throws(Exception::class)
    fun `GET employees with filtering parameters returns 200`() {
        val requestBuilder = get("/api/employees")
            .param("page", "0")
            .param("position", "Developer")

        mockMvc.perform(requestBuilder)
            .andExpect(status().isOk())
    }

    @Test
    @WithMockUser(authorities = ["ROLE_USER"])
    @Throws(Exception::class)
    fun `POST employees with 'user' MockUser returns 403`() {
        val requestBuilder = post("/api/employees")

        mockMvc.perform(requestBuilder)
            .andExpect(status().isForbidden())
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    @Throws(Exception::class)
    fun `POST employees with 'ROLE_ADMIN' MockUser returns 200`() {
        val requestBuilder = post("/api/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "name": "John Doe",
                    "phone": "085111111111",
                    "position": "Software Engineer"
                }
            """.trimIndent()
            )

        mockMvc.perform(requestBuilder)
            .andExpect(status().isCreated())
    }

    @Test
    @Throws(Exception::class)
    fun `PUT employee without authorization returns 401`() {
        val employeeId = UUID.randomUUID()
        val requestBuilder = put("/api/employees/$employeeId")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "id": "$employeeId",
                    "name": "Updated Name",
                    "phone": "085222222222",
                    "position": "Senior Engineer"
                }
            """.trimIndent()
            )

        mockMvc.perform(requestBuilder)
            .andExpect(status().isUnauthorized())
    }

    @Test
    @WithMockUser(authorities = ["ROLE_USER"])
    @Throws(Exception::class)
    fun `PUT employee with 'ROLE_USER' MockUser returns 403`() {
        val employeeId = UUID.randomUUID()
        val requestBuilder = put("/api/employees/$employeeId")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "id": "$employeeId",
                    "name": "Updated Name",
                    "phone": "085222222222",
                    "position": "Senior Engineer"
                }
            """.trimIndent()
            )

        mockMvc.perform(requestBuilder)
            .andExpect(status().isForbidden())
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    @Throws(Exception::class)
    fun `PUT employee with 'ROLE_ADMIN' MockUser returns 200`() {
        val employeeId = UUID.randomUUID()

        val requestBuilder = put("/api/employees/$employeeId")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "id": "$employeeId",
                    "name": "Updated Name",
                    "phone": "085222222222",
                    "position": "Senior Engineer",
                    "email": "updated@example.com"
                }
            """.trimIndent()
            )

        mockMvc.perform(requestBuilder)
            .andExpect(status().isOk())
    }

    @Test
    @Throws(Exception::class)
    fun `DELETE employee without authorization returns 401`() {
        val employeeId = UUID.randomUUID()
        val requestBuilder = delete("/api/employees/$employeeId")

        mockMvc.perform(requestBuilder)
            .andExpect(status().isUnauthorized())
    }

    @Test
    @WithMockUser(authorities = ["ROLE_USER"])
    @Throws(Exception::class)
    fun `DELETE employee with 'ROLE_USER' MockUser returns 403`() {
        val employeeId = UUID.randomUUID()
        val requestBuilder = delete("/api/employees/$employeeId")

        mockMvc.perform(requestBuilder)
            .andExpect(status().isForbidden())
    }

    @Test
    @WithMockUser(authorities = ["ROLE_ADMIN"])
    @Throws(Exception::class)
    fun `DELETE employee with 'ROLE_ADMIN' MockUser returns 204`() {
        val employeeId = UUID.randomUUID()
        val requestBuilder = delete("/api/employees/$employeeId")

        mockMvc.perform(requestBuilder)
            .andExpect(status().isNoContent())
    }
}
