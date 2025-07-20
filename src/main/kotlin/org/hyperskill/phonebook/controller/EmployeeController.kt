package org.hyperskill.phonebook.controller


import jakarta.validation.Valid
import org.hyperskill.phonebook.dtos.request.employee.CreateEmployeeRequest
import org.hyperskill.phonebook.dtos.request.employee.UpdateEmployeeRequest
import org.hyperskill.phonebook.model.Employee
import org.hyperskill.phonebook.service.EmployeeServices
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/employees")

class EmployeeController(
    private val employeeServices: EmployeeServices
) {

    @GetMapping
    fun getEmployees(
        @RequestParam page: Int = 0,
        @RequestParam(required = false) departmentId: UUID?,
        @RequestParam(required = false) position: String?
    ): ResponseEntity<Page<Employee>> {
        if (page < 0) return ResponseEntity.badRequest().build()

        val employees = employeeServices.getEmployeesFiltered(page, departmentId, position)
        return ResponseEntity.ok(employees)
    }


    @PostMapping
    fun store(@RequestBody @Valid createEmployeeRequest: CreateEmployeeRequest): ResponseEntity<Employee> {
        val employee = employeeServices.store(createEmployeeRequest)
        return ResponseEntity(employee, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updateEmployee(
        @PathVariable id: UUID,
        @RequestBody @Valid updateEmployeeRequest: UpdateEmployeeRequest
    ): ResponseEntity<Employee> {
        val employee = employeeServices.updateEmployee(id, updateEmployeeRequest)
        return ResponseEntity(employee, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteEmployee(@PathVariable id: UUID): ResponseEntity<Void> {
        employeeServices.deleteEmployee(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
