package org.hyperskill.phonebook.service

import org.hyperskill.phonebook.dtos.CreateEmployeeRequest
import org.hyperskill.phonebook.model.Employee
import org.hyperskill.phonebook.repository.DepartmentRepository
import org.hyperskill.phonebook.repository.EmployeeRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class EmployeeServices(
    private val employeeRepository: EmployeeRepository,
    private val departmentRepository: DepartmentRepository
) {

    fun getAllEmployees(page: Int): Page<Employee> {
        val pageRequest = PageRequest.of(page, 10)
        return employeeRepository.findAll(pageRequest)
    }

    fun store(createEmployeeRequest: CreateEmployeeRequest): Employee {
        val department = createEmployeeRequest.departmentId?.let { departmentRepository.findByIdOrNull(it) }
        val employee = Employee(
            name = createEmployeeRequest.name,
            position = createEmployeeRequest.position,
            phone = createEmployeeRequest.phone,
            email = createEmployeeRequest.email,
            department = department
        )

        return employeeRepository.save(employee)
    }
}
