package org.hyperskill.phonebook.controller

import org.hyperskill.phonebook.service.AuthService
import org.hyperskill.phonebook.service.JwtService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.*
import org.hyperskill.phonebook.service.UserPhonebookService
import org.hyperskill.phonebook.dtos.Response
import org.hyperskill.phonebook.dtos.Request

@RestController
@RequestMapping("api/caching")
class CachingController(
    private val userPhonebookService: UserPhonebookService
) {

    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: String): String {
        return userPhonebookService.getUser(userId)
    }

    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable userId: String): String {
        return  userPhonebookService.deleteUser(userId)
    }

    // look update controller and use the same method
    //@PathVariable id: UUID
}