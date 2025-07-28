package org.hyperskill.phonebook.dtos.response

import java.time.LocalDateTime

data class ErrorResponse(
    private val statusCode: Int,
    private val errorMessage: String,
    val description: String? = null,
    private val responseTimestamp: LocalDateTime = LocalDateTime.now()
) : BaseResponse(
    status = statusCode,
    message = errorMessage,
    timestamp = responseTimestamp,
    success = false
)
