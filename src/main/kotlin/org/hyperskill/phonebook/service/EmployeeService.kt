package org.hyperskill.phonebook.service

import jakarta.persistence.EntityNotFoundException
import org.hyperskill.phonebook.dtos.request.employee.CreateEmployeeRequest
import org.hyperskill.phonebook.dtos.request.employee.UpdateEmployeeRequest
import org.hyperskill.phonebook.model.Employee
import org.hyperskill.phonebook.repository.DepartmentRepository
import org.hyperskill.phonebook.repository.EmployeeRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import java.util.UUID

@Service
class EmployeeService(
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
            name = createEmployeeRequest.name.orEmpty(),
            position = createEmployeeRequest.position.orEmpty(),
            phone = createEmployeeRequest.phone.orEmpty(),
            email = createEmployeeRequest.email,
            department = department
        )

        return employeeRepository.save(employee)
    }

    fun updateEmployee(id: UUID, employee: UpdateEmployeeRequest): Employee {
        employeeRepository.findByIdOrNull(id)
            ?: throw EntityNotFoundException("Employee with id=$id not found")
        val department = employee.departmentId?.let { departmentRepository.findById(it).orElse(null) }
        return employeeRepository.save(Employee(
            id = id,
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

    @CachePut("cachePhoneBook", key = "#userId")
    fun putUser(@PathVariable userId: String, @RequestBody employee: Employee): String {
        println("Putting user data for $userId")
        return "User data for $userId"
    }
}
