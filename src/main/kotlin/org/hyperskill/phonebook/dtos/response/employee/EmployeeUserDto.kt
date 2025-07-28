package org.hyperskill.phonebook.dtos.response.employee

import org.hyperskill.phonebook.model.Employee
import java.util.*

data class EmployeeUserDto(
    val id: UUID?,
    val name: String,
    val position: String,
    val department: String?,
    val division: String?,
    val phone: String,
    val email: String?
) {
    companion object {
        fun fromEmployee(employee: Employee): EmployeeUserDto {
            return EmployeeUserDto(
                id = employee.id,
                name = employee.name,
                position = employee.position,
                phone = employee.phone,
                email = employee.email,
                department = employee.department?.name,
                division = employee.department?.division?.name
            )
        }
    }
}
