package org.hyperskill.phonebook.dtos.response.employee

import org.hyperskill.phonebook.model.Department
import org.hyperskill.phonebook.model.Employee
import java.time.Instant
import java.util.*

data class EmployeeAdminDto(
    val id: UUID?,
    val name: String,
    val position: String,
    val phone: String,
    val email: String?,
    val department: Department?,
    val createdAt: Instant,
    val updatedAt: Instant,
    val isActive: Boolean
) {
    companion object {
        fun fromEmployee(employee: Employee): EmployeeAdminDto {
            return EmployeeAdminDto(
                id = employee.id,
                name = employee.name,
                position = employee.position,
                phone = employee.phone,
                email = employee.email,
                department = employee.department,
                createdAt = employee.createdAt,
                updatedAt = employee.updatedAt,
                isActive = employee.isActive
            )
        }
    }
}
