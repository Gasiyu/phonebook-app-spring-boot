package org.hyperskill.phonebook.controller

import org.hyperskill.phonebook.service.AuthService
import org.hyperskill.phonebook.service.JwtService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.hyperskill.phonebook.dtos.Response
import org.hyperskill.phonebook.dtos.Request

@RestController
@RequestMapping("api")
class AuthController(
    private val authService: AuthService,
    private val jwtService: JwtService,
) {
    @PostMapping("login")
    fun login(@RequestBody body: Request): Response {
        val user = authService.authenticate(body.username, body.password)
        return Response(
            jwtService.generateToken(user),
            jwtService.getExpiresIn(),
        )
    }
}