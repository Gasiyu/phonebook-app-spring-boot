package org.hyperskill.phonebook.service

import jakarta.persistence.EntityNotFoundException
import org.hyperskill.phonebook.dtos.request.employee.CreateEmployeeRequest
import org.hyperskill.phonebook.dtos.request.employee.UpdateEmployeeRequest
import org.hyperskill.phonebook.model.Employee
import org.hyperskill.phonebook.repository.DepartmentRepository
import org.hyperskill.phonebook.repository.EmployeeRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class EmployeeService(
    private val employeeRepository: EmployeeRepository,
    private val departmentRepository: DepartmentRepository
) {

    @Cacheable(value = ["employees"], key = "'all-page-' + #page", condition = "#page >= 0")
    fun getAllEmployees(page: Int): Page<Employee> {
        val pageRequest = PageRequest.of(page, 10)
        return employeeRepository.findAll(pageRequest)
    }

    @CachePut(value = ["employees"], key = "#result.id")
    @Caching(
        evict = [
            CacheEvict(value = ["employees"], key = "'all-page-*'", allEntries = true),
            CacheEvict(value = ["employees"], key = "'filtered-page-*'", allEntries = true)
        ],
        put = [CachePut(value = ["employees"], key = "#result.id")]
    )
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

    @Cacheable(value = ["employees"], key = "#id")
    fun getEmployee(id: UUID): Employee {
        return employeeRepository.findByIdOrNull(id)
            ?: throw EntityNotFoundException("Employee with id=$id not found")
    }

    @Caching(
        evict = [
            CacheEvict(value = ["employees"], key = "'all-page-*'", allEntries = true),
            CacheEvict(value = ["employees"], key = "'filtered-page-*'", allEntries = true)
        ],
        put = [CachePut(value = ["employees"], key = "#id")]
    )
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

    @Cacheable(
        value = ["employees"],
        key = "'filtered-page-' + #page + '-dept-' + #departmentId + '-pos-' + #position",
        condition = "#page >= 0"
    )
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

    @Caching(
        evict = [
            CacheEvict(value = ["employees"], key = "#id"),
            CacheEvict(value = ["employees"], key = "'all-page-*'", allEntries = true),
            CacheEvict(value = ["employees"], key = "'filtered-page-*'", allEntries = true)
        ]
    )
    fun deleteEmployee(id: UUID) {
        val employee = employeeRepository.findByIdOrNull(id)
            ?: throw EntityNotFoundException("Employee with id=$id not found")
        employeeRepository.delete(employee)
    }
}
