package org.hyperskill.phonebook.controller

import org.hyperskill.phonebook.service.UserPhonebookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.cache.CacheManager



@SpringBootTest
class CacheIntegrationTest {

    @Autowired
    lateinit var myService: UserPhonebookService

    @Autowired
    lateinit var cacheManager: CacheManager

    @Test
    fun mockReturn() {
        val userId = "cachePhoneBook"
        val result = myService.getUser(userId)

        val cachedValue = cacheManager
            .getCache("cachePhoneBook")
            ?.get(userId, String::class.java)

        assertEquals(result, cachedValue)
    }
}

