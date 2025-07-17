package org.hyperskill.phonebook.service


import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val userAuthService: UserAuthService,
) {
    fun authenticate(username: String, password: String): UserDetails {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
        return userAuthService.loadUserByUsername(username)
    }
}