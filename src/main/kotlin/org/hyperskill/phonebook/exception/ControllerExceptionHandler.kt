package org.hyperskill.phonebook.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime

@ControllerAdvice
class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        ex: MethodArgumentNotValidException,
        request: WebRequest
    ): ResponseEntity<CustomErrorMessage> {
        val errorMessage = ex.bindingResult.fieldErrors.firstOrNull()?.defaultMessage ?: "Invalid request"
        return ResponseEntity(
            CustomErrorMessage(
                status = 400,
                timestamp = LocalDateTime.now(),
                error = "Bad Request",
                message = errorMessage,
                path = request.getDescription(false)
            ),
            HttpStatus.BAD_REQUEST
        )
    }
}

data class CustomErrorMessage(
    val status: Int,
    val timestamp: LocalDateTime,
    val error: String?,
    val message: String?,
    val path: String?,
)
