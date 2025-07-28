package org.hyperskill.phonebook.dtos.response

import java.time.LocalDateTime

abstract class BaseResponse(
    val status: Int,
    val message: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val success: Boolean
)
