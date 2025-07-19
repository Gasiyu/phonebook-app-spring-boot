package org.hyperskill.phonebook.exception

import org.hyperskill.phonebook.dtos.response.ErrorResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(ControllerExceptionHandler::class.java)
    }
}
