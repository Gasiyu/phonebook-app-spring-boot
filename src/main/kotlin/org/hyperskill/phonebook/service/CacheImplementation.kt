package org.hyperskill.phonebook.service

import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable


@Service
class UserPhonebookService {

    @Cacheable("cachePhoneBook")
    fun getUser(@PathVariable userId: String): String {
        println("Fetching from DB...")
        return "User data for $userId"
    }

    @CacheEvict("cachePhoneBook", key = "#userId")
    fun deleteUser(@PathVariable userId: String): String {
        println("Evicting cache...")
        return  "User data for $userId"
    }
}
