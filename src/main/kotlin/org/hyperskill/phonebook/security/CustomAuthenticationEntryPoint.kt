package org.hyperskill.phonebook.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.hyperskill.phonebook.dtos.response.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {

    private val logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint::class.java)

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        logger.error("Authentication error: {}", authException.message, authException)

        val errorResponse = ErrorResponse(
            statusCode = HttpStatus.UNAUTHORIZED.value(),
            errorMessage = HttpStatus.UNAUTHORIZED.reasonPhrase,
            description = "Authentication failed: ${authException.message ?: "Invalid credentials or missing authentication"}"
        )

        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }
}
