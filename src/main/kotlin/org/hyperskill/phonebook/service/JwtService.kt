package org.hyperskill.phonebook.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService {
    @Value("\${security.jwt.secret-key}")
    private val secretKey: String = "defaultSecretKey"

    @Value("\${security.jwt.expiration-time}")
    private val expiresIn: Long = 0

    fun getExpiresIn(): Long {
        return expiresIn
    }

    fun generateToken(user: UserDetails): String {
        val secret = Keys.hmacShaKeyFor(secretKey.toByteArray());

        return Jwts.builder()
            .subject(user.username)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + expiresIn)) // 10 hours
            .signWith(secret)
            .compact()
    }

    private fun extractAllClaims(token: String): Claims {
        val secret = Keys.hmacShaKeyFor(secretKey.toByteArray());

        return Jwts.parser()
            .verifyWith(secret)
            .build()
            .parseSignedClaims(token)
            .payload
    }

    fun extractUsername(token: String): String {
        return extractAllClaims(token).subject
    }

    private fun extractExpirationDate(token: String): Date {
        return extractAllClaims(token).expiration
    }

    private fun isExpired(token: String): Boolean {
        return extractExpirationDate(token).before(Date())
    }

    fun isValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return (username == userDetails.username && !isExpired(token))
    }
}