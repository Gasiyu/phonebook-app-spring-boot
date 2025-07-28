package org.hyperskill.phonebook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class PhonebookApplication

fun main(args: Array<String>) {
	runApplication<PhonebookApplication>(*args)
}
