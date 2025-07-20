package org.hyperskill.phonebook.exception

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SignatureException
import jakarta.persistence.EntityNotFoundException
import org.hyperskill.phonebook.dtos.response.ErrorResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        ex: MethodArgumentNotValidException
    ): ResponseEntity<ErrorResponse> {
        val errorMessage = ex.bindingResult.fieldErrors.firstOrNull()?.defaultMessage ?: ex.message
        return ResponseEntity(
            ErrorResponse(
                statusCode = 400,
                errorMessage = HttpStatus.BAD_REQUEST.reasonPhrase,
                description = errorMessage
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(MalformedJwtException::class)
    fun handleException(
        ex: MalformedJwtException
    ): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(
                statusCode = 401,
                errorMessage = HttpStatus.UNAUTHORIZED.reasonPhrase,
                description = "Invalid JWT token"
            ),
            HttpStatus.UNAUTHORIZED
        )
    }

    @ExceptionHandler(ExpiredJwtException::class)
    fun handleException(
        ex: ExpiredJwtException
    ): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(
                statusCode = 401,
                errorMessage = HttpStatus.UNAUTHORIZED.reasonPhrase,
                description = ex.message
            ),
            HttpStatus.UNAUTHORIZED
        )
    }

    @ExceptionHandler(SignatureException::class)
    fun handleException(
        ex: SignatureException
    ): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(
                statusCode = 401,
                errorMessage = HttpStatus.UNAUTHORIZED.reasonPhrase,
                description = ex.message
            ),
            HttpStatus.UNAUTHORIZED
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleException(
        ex: HttpMessageNotReadableException
    ): ResponseEntity<ErrorResponse> {
        LOGGER.error("Error parsing request body", ex)
        return ResponseEntity(
            ErrorResponse(
                statusCode = 400,
                errorMessage = "Invalid Request Format",
                description = ex.message
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(
        exception = [
            AuthorizationDeniedException::class,
            AccessDeniedException::class
        ]
    )
    fun handleException(
        ex: RuntimeException
    ): ResponseEntity<ErrorResponse> {
        LOGGER.error("Unauthorized to access this resource", ex)
        return ResponseEntity(
            ErrorResponse(
                statusCode = 403,
                errorMessage = HttpStatus.FORBIDDEN.reasonPhrase,
                description = "Unauthorized to access this resource: Insufficient permissions to perform this action."
            ),
            HttpStatus.FORBIDDEN
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(
        ex: Exception
    ): ResponseEntity<ErrorResponse> {
        LOGGER.error("Unhandled exception occurred", ex)
        return ResponseEntity(
            ErrorResponse(
                statusCode = 500,
                errorMessage = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
                description = "An unexpected error occurred"
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleException(
        ex: EntityNotFoundException
    ): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(
                statusCode = 404,
                errorMessage = HttpStatus.NOT_FOUND.reasonPhrase,
                description = ex.message
            ),
            HttpStatus.NOT_FOUND
        )
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(ControllerExceptionHandler::class.java)
    }
}
