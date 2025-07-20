package org.hyperskill.phonebook.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.hyperskill.phonebook.SecurityConfig
import org.hyperskill.phonebook.dtos.JwtTokenResult
import org.hyperskill.phonebook.dtos.request.auth.LoginRequest
import org.hyperskill.phonebook.security.CustomAccessDeniedHandler
import org.hyperskill.phonebook.security.CustomAuthenticationEntryPoint
import org.hyperskill.phonebook.service.AuthService
import org.hyperskill.phonebook.service.JwtService
import org.hyperskill.phonebook.service.UserAuthService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(AuthController::class)
@Import(SecurityConfig::class)
internal class AuthControllerTest(
    @Autowired private val mockMvc: MockMvc
) {

    @field:MockitoBean
    private lateinit var authService: AuthService

    @field:MockitoBean
    private lateinit var jwtService: JwtService

    @field:MockitoBean
    private lateinit var customAccessDeniedHandler: CustomAccessDeniedHandler

    @field:MockitoBean
    private lateinit var customAuthenticationEntryPoint: CustomAuthenticationEntryPoint

    @Suppress("unused")
    @field:MockitoBean
    private lateinit var userAuthService: UserAuthService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val mockUserDetails: UserDetails = User
        .withUsername("testUser")
        .password("password")
        .authorities(emptyList())
        .build()

    private val mockToken =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTY5MDIwMDAwMCwiZXhwIjoxNjkwMjAzNjAwfQ.signature"
    private val mockExpiresAt = 1690203600000L
    private val mockExpiresInSeconds = 3600L

    @Test
    @Throws(Exception::class)
    fun `login with valid credentials should return successful response with JWT token`() {
        val loginRequest = LoginRequest(username = "testUser", password = "password")
        val jwtTokenResult = JwtTokenResult(token = mockToken, expiresAt = mockExpiresAt)

        `when`(authService.authenticate("testUser", "password")).thenReturn(mockUserDetails)
        `when`(jwtService.generateToken(mockUserDetails)).thenReturn(jwtTokenResult)
        `when`(jwtService.getExpiresIn()).thenReturn(mockExpiresInSeconds)

        val requestBuilder = post("/api/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest))

        mockMvc.perform(requestBuilder)
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.message").value("Login successful"))
            .andExpect(jsonPath("$.data.accessToken").value(mockToken))
            .andExpect(jsonPath("$.data.expiresInSeconds").value(mockExpiresInSeconds))
            .andExpect(jsonPath("$.data.expiresAt").value(mockExpiresAt))
    }

    @Test
    @Throws(Exception::class)
    fun `login with invalid JSON format should return 400 Bad Request`() {
        val invalidJson = "{ invalid json }"

        val requestBuilder = post("/api/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidJson)

        mockMvc.perform(requestBuilder)
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("Invalid Request Format"))
    }

    @Test
    @Throws(Exception::class)
    fun `login with blank username and password should return 400 Bad Request`() {
        val loginRequest = LoginRequest(username = "", password = "")

        val requestBuilder = post("/api/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest))

        mockMvc.perform(requestBuilder)
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("Bad Request"))
    }

    @Test
    @Throws(Exception::class)
    fun `login with incorrect credentials should return 401 Unauthorized`() {
        val loginRequest = LoginRequest(username = "testUser", password = "wrongPassword")

        `when`(authService.authenticate("testUser", "wrongPassword"))
            .thenThrow(BadCredentialsException("Bad credentials"))

        val requestBuilder = post("/api/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest))

        mockMvc.perform(requestBuilder)
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.status").value(401))
            .andExpect(jsonPath("$.message").value("Unauthorized"))
            .andExpect(jsonPath("$.description").value("Invalid username or password provided"))
    }
}
