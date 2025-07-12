package org.hyperskill.phonebook.controller

import org.hyperskill.phonebook.dtos.AuthRequest
import org.hyperskill.phonebook.service.AuthService
import org.hyperskill.phonebook.util.JwtUtil
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val jwtUtil: JwtUtil,
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(@RequestBody authRequest: AuthRequest): ResponseEntity<Map<String, String>> {
        if (authRequest.username == "admin" && authRequest.password == "admin") {
            val token = jwtUtil.generateToken(authRequest.username)
            return ResponseEntity.ok(mapOf("token" to token))
        }
        return ResponseEntity.status(401).body(mapOf("error" to "Unauthorized"))
    }
}


