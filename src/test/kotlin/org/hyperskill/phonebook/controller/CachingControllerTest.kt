package org.hyperskill.phonebook.controller

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import io.mockk.MockKAnnotations
import org.hyperskill.phonebook.service.UserPhonebookService
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test
import io.mockk.every
import io.mockk.verify
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.web.bind.annotation.PathVariable


@WebMvcTest
class MyServiceUnitTest {

    @MockK
    lateinit var myService: UserPhonebookService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should call service once and use mocked return`() {
        every { myService.getUser(userId = "#userId") } returns "Cached Data"

        val result1 = myService.getUser(userId = "#userId")
        val result2 = myService.getUser(userId = "#userId")

        verify(exactly = 2) { myService.getUser(userId = "#userId") } // 2 calls to mock, no cache involved here
        assertEquals("Cached Data", result1)
        assertEquals("Cached Data", result2)
    }
}
/*@SpringBootTest
class CacheIntegrationTest {

    @Autowired
    lateinit var myService: UserPhonebookService

    @Autowired
    lateinit var cacheManager: UserPhonebookService

    @Test
    fun `should populate cache after method call`() {
        val result = myService.getUser(userId = "cachePhoneBook")

        val cached = cacheManager.getUser("cachePhoneBook")[42].get()
        assertEquals(result, cached)
    }
}*/

