package org.hyperskill.phonebook.controller


import jakarta.validation.Valid
import org.hyperskill.phonebook.dtos.request.employee.CreateEmployeeRequest
import org.hyperskill.phonebook.dtos.request.employee.UpdateEmployeeRequest
import org.hyperskill.phonebook.dtos.response.PageDto
import org.hyperskill.phonebook.dtos.response.SuccessResponse
import org.hyperskill.phonebook.dtos.response.employee.EmployeeAdminDto
import org.hyperskill.phonebook.dtos.response.employee.EmployeeUserDto
import org.hyperskill.phonebook.service.EmployeeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/employees")

class EmployeeController(
    private val employeeService: EmployeeService
) {

    @GetMapping
    fun getEmployees(
        @RequestParam page: Int = 0,
        @RequestParam(required = false) departmentId: UUID?,
        @RequestParam(required = false) position: String?
    ): ResponseEntity<SuccessResponse<PageDto<*>>> {
        if (page < 0) return ResponseEntity.badRequest().build()

        val employees = employeeService.getEmployeesFiltered(page, departmentId, position)

        val authentication = SecurityContextHolder.getContext().authentication
        val isAdmin = authentication.authorities.any { it.authority == "ROLE_ADMIN" }

        val pageDto = if (isAdmin) {
            PageDto.fromPage(employees) { EmployeeAdminDto.fromEmployee(it) }
        } else {
            PageDto.fromPage(employees) { EmployeeUserDto.fromEmployee(it) }
        }

        return ResponseEntity.ok(
            SuccessResponse(
                statusCode = 200,
                successMessage = "Employees retrieved successfully",
                data = pageDto
            )
        )
    }

    @GetMapping("/{id}")
    fun getEmployee(
        @PathVariable id: UUID
    ): ResponseEntity<SuccessResponse<*>> {
        val employee = employeeService.getEmployee(id)

        val authentication = SecurityContextHolder.getContext().authentication
        val isAdmin = authentication.authorities.any { it.authority == "ROLE_ADMIN" }

        val employeeDto = if (isAdmin) {
            EmployeeAdminDto.fromEmployee(employee)
        } else {
            EmployeeUserDto.fromEmployee(employee)
        }

        return ResponseEntity.ok(
            SuccessResponse(
                statusCode = 200,
                successMessage = "Employees retrieved successfully",
                data = employeeDto
            )
        )
    }

    @PostMapping
    fun store(@RequestBody @Valid createEmployeeRequest: CreateEmployeeRequest): ResponseEntity<SuccessResponse<EmployeeAdminDto>> {
        val employee = employeeService.store(createEmployeeRequest)
        val employeeDto = EmployeeAdminDto.fromEmployee(employee)
        return ResponseEntity(
            SuccessResponse(
                statusCode = 201,
                successMessage = "Employee created successfully",
                data = employeeDto
            ),
            HttpStatus.CREATED
        )
    }

    @PutMapping("/{id}")
    fun updateEmployee(
        @PathVariable id: UUID,
        @RequestBody @Valid updateEmployeeRequest: UpdateEmployeeRequest
    ): ResponseEntity<SuccessResponse<EmployeeAdminDto>> {
        val employee = employeeService.updateEmployee(id, updateEmployeeRequest)
        val employeeDto = EmployeeAdminDto.fromEmployee(employee)
        return ResponseEntity(
            SuccessResponse(
                statusCode = 200,
                successMessage = "Employee updated successfully",
                data = employeeDto
            ),
            HttpStatus.OK
        )
    }

    @DeleteMapping("/{id}")
    fun deleteEmployee(@PathVariable id: UUID): ResponseEntity<SuccessResponse<Nothing?>> {
        employeeService.deleteEmployee(id)
        return ResponseEntity(
            SuccessResponse(
                statusCode = 204,
                successMessage = "Employee deleted successfully",
                data = null
            ),
            HttpStatus.NO_CONTENT
        )
    }
}
