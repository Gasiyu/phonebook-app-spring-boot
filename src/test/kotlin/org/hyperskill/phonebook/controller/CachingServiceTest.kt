package org.hyperskill.phonebook.controller

import org.hyperskill.phonebook.service.EmployeeServices
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.cache.CacheManager


@SpringBootTest
class CacheIntegrationTest {

    @Autowired
    lateinit var myService: EmployeeServices

    @Autowired
    lateinit var cacheManager: CacheManager

    @Test
    fun mockReturn() {
        val userId = "cachePhoneBook"
        val result = myService.getUser(userId)
        val cacheEvict = myService.deleteUser(userId)
        val cachePut = myService.putUser(userId)

        val cachedValue = cacheManager
            .getCache("cachePhoneBook")
            ?.get(userId, String::class.java)

        assertEquals(result, cachedValue)
        assertEquals("User data for $userId", cacheEvict)
        assertEquals("User data for $userId", cachePut)
    }
}

