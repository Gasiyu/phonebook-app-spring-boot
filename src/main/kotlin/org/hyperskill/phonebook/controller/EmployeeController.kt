package org.hyperskill.phonebook.controller


import org.hyperskill.phonebook.model.Employee
import org.hyperskill.phonebook.service.EmployeeServices
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/employees")

class EmployeeController(
    private val employeeServices: EmployeeServices
) {

    @GetMapping
    fun getAllEmployees(@RequestParam page: Int = 0): Page<Employee> {
        return employeeServices.getAllEmployees(page)
    }

}