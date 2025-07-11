package org.hyperskill.phonebook.service

import org.hyperskill.phonebook.dtos.CreateEmployeeRequest
import org.hyperskill.phonebook.model.Employee
import org.hyperskill.phonebook.repository.DepartmentRepository
import org.hyperskill.phonebook.repository.EmployeeRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.hyperskill.phonebook.dtos.UpdateEmployeeRequest
import javax.persistence.EntityNotFoundException
import java.util.UUID

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

    fun updateEmployee(employee: UpdateEmployeeRequest): Employee {
        val department = employee.departmentId?.let { departmentRepository.findById(it).orElse(null) }
        return employeeRepository.save(Employee(
            id = employee.id,
            name = employee.name,
            position = employee.position,
            phone = employee.phone,
            email = employee.email,
            department = department
        ))
    }

    fun getEmployeesFiltered(
        page: Int,
        departmentId: UUID? = null,
        position: String? = null
    ): Page<Employee> {
        val pageRequest = PageRequest.of(page, 10)

        return when {
            departmentId != null && !position.isNullOrBlank() ->
                employeeRepository.findByDepartmentIdAndPositionContainingIgnoreCase(departmentId, position, pageRequest)
            departmentId != null ->
                employeeRepository.findByDepartmentId(departmentId, pageRequest)
            !position.isNullOrBlank() ->
                employeeRepository.findByPositionContainingIgnoreCase(position, pageRequest)
            else ->
                employeeRepository.findAll(pageRequest)
        }
    }

    fun deleteEmployee(id: UUID) {
        val employee = employeeRepository.findByIdOrNull(id)
            ?: throw EntityNotFoundException("Employee with id=$id not found")
        employeeRepository.delete(employee)
    }

}
