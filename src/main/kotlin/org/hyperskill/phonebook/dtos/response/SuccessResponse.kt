package org.hyperskill.phonebook.dtos.response

import java.time.LocalDateTime

data class SuccessResponse<T>(
    private val statusCode: Int,
    private val successMessage: String,
    val data: T,
    private val responseTimestamp: LocalDateTime = LocalDateTime.now()
) : BaseResponse(
    status = statusCode,
    message = successMessage,
    timestamp = responseTimestamp,
    success = true
)
