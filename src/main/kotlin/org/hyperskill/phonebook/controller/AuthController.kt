package org.hyperskill.phonebook.controller

import jakarta.validation.Valid
import org.hyperskill.phonebook.dtos.request.auth.LoginRequest
import org.hyperskill.phonebook.dtos.response.SuccessResponse
import org.hyperskill.phonebook.dtos.response.auth.LoginResponse
import org.hyperskill.phonebook.service.AuthService
import org.hyperskill.phonebook.service.JwtService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api")
class AuthController(
    private val authService: AuthService,
    private val jwtService: JwtService
) {
    @PostMapping("login")
    fun login(@RequestBody @Valid body: LoginRequest): ResponseEntity<SuccessResponse<LoginResponse>> {
        val user = authService.authenticate(body.username.orEmpty(), body.password.orEmpty())
        val tokenResult = jwtService.generateToken(user)

        return ResponseEntity(
            SuccessResponse(
                statusCode = 200,
                successMessage = "Login successful",
                data = LoginResponse(
                    accessToken = tokenResult.token,
                    expiresInSeconds = jwtService.getExpiresIn(),
                    expiresAt = tokenResult.expiresAt
                )
            ),
            HttpStatus.OK
        )
    }
}
