package org.hyperskill.phonebook.service

import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class UserPhonebookService {

    @Cacheable("cachePhoneBook")
    fun getUser(userId: Long): String {
        println("Fetching from DB...")
        return "User data for $userId"
    }

    @CacheEvict("cachePhoneBook", key = "#userId")
    fun deleteUser(userId: Long) {
        println("Evicting cache...")
    }
}
