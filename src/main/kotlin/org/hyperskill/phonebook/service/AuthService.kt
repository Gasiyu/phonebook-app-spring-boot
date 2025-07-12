package org.hyperskill.phonebook.service

import org.springframework.stereotype.Service

@Service
class AuthService {

    fun authenticate(username: String, password: String): Boolean {
        return username == "admin" && password == "admin"
    }
}