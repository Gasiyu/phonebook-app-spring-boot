package org.hyperskill.phonebook.exception

import java.util.*

class Error(
    val status: Int,
    val error: String,
    val message: String?,
    val timestamp: Date = Date(),
)