package org.hyperskill.phonebook.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.hyperskill.phonebook.dtos.response.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class CustomAccessDeniedHandler(
    private val objectMapper: ObjectMapper
) : AccessDeniedHandler {

    private val logger = LoggerFactory.getLogger(CustomAccessDeniedHandler::class.java)

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        logger.error("Access denied error: {}", accessDeniedException.message, accessDeniedException)

        val errorResponse = ErrorResponse(
            statusCode = HttpStatus.FORBIDDEN.value(),
            errorMessage = HttpStatus.FORBIDDEN.reasonPhrase,
            description = "Unauthorized to access this resource: Insufficient permissions to perform this action."
        )

        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpStatus.FORBIDDEN.value()
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }
}
