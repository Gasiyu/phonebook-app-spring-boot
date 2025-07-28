package org.hyperskill.phonebook.exception

import org.hyperskill.phonebook.controller.AuthController
import org.hyperskill.phonebook.dtos.response.ErrorResponse
import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice(assignableTypes = [AuthController::class])
class AuthControllerAdvice {

    @ExceptionHandler(BadCredentialsException::class)
    fun handleException(
        ex: BadCredentialsException
    ): ResponseEntity<ErrorResponse> {
        LOGGER.error("Invalid username or password provided", ex)
        return ResponseEntity(
            ErrorResponse(
                statusCode = 401,
                errorMessage = HttpStatus.UNAUTHORIZED.reasonPhrase,
                description = "Invalid username or password provided"
            ),
            HttpStatus.UNAUTHORIZED
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleException(
        ex: MethodArgumentNotValidException
    ): ResponseEntity<ErrorResponse> {
        LOGGER.error("Error validating request body", ex)
        return ResponseEntity(
            ErrorResponse(
                statusCode = 400,
                errorMessage = HttpStatus.BAD_REQUEST.reasonPhrase,
                description = ex.bindingResult.fieldErrors.firstOrNull()?.defaultMessage ?: ex.message
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    companion object {
        private val LOGGER: Logger = org.slf4j.LoggerFactory.getLogger(AuthControllerAdvice::class.java)
    }
}
