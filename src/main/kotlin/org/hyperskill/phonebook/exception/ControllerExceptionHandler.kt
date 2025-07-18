package org.hyperskill.phonebook.exception

import org.hyperskill.phonebook.dtos.response.ErrorResponse
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
}
