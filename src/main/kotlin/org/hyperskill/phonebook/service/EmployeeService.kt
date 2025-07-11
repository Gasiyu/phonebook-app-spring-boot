package org.hyperskill.phonebook.service

import org.hyperskill.phonebook.model.Employee
import org.hyperskill.phonebook.repository.EmployeeRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class EmployeeServices(
    private val employeeRepository: EmployeeRepository,
) {

    fun getAllEmployees(page: Int): Page<Employee> {
        val pageRequest = PageRequest.of(page, 10)
        return employeeRepository.findAll(pageRequest)
    }

}